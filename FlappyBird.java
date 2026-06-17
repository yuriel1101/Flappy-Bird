import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;


public class FlappyBird extends JPanel implements ActionListener, KeyListener, MouseListener {
	int boardWidth = 360;
	int boardHeight = 640;
	Random random = new Random();

	// Images
	Image backgroundImg;
	Image birdImg;
	Image topPipeImg;
	Image bottomPipeImg;
	Image gameOverImg;
	Image startButtonImg;

	// Bird stuff
	int birdX = boardWidth/7;
	int birdY = boardHeight/2;
	int birdWidth = 34;
	int birdHeight = 24;

	class Bird {
		int x = birdX;
		int y = birdY;
		int centerX = birdX/2;
		int centerY = birdY/2;
		int width = birdWidth;
		int height = birdHeight;
		Image img;

		Bird(Image img) {
			this.img = img;
		}
	}
	
	// pipes
	int pipeX = boardWidth;
	int pipeY = 0;
	int pipeWidth = 64;
	int pipeHeight = 512;

	class Pipe {
		int x = pipeX;
		int y = pipeY;
		int centerX = pipeX/2;
		int width = pipeWidth;
		int height = pipeHeight;
		Image img;
		boolean passed = false;
		boolean top = false;

		Pipe(Image img) {
			this.img = img;
		}
	}



	// buttons
	class Button {
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		int centerX = x + (width/2);
		int centerY = y + (height/2);
		Image img;

		Button(Image img) {
			this.img = img;
		}
	}



	//game logic
	Bird bird;
	int velocityX = -4;
	int velocityY = -10;
	int fallSpeed = 1;

	int score = 0;

	int gameFrame = 0;
	int startFrameCounter = -3;

	boolean justLaunched = true;
	boolean gameLost = false;
	boolean moveState = false;

	Timer gameLoop;
	Timer pipeTimer;

	ArrayList<Pipe> pipes;

	// setting up button dimensions
	Button startButton;
	

	boolean pressedInButton = false;


	
	FlappyBird() {
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		setFocusable(true);
		addKeyListener(this);
		addMouseListener(this);

		// load images
		backgroundImg = new ImageIcon(getClass().getResource("sprites/flappybirdbg.png")).getImage();
		birdImg = new ImageIcon(getClass().getResource("sprites/flappybird.png")).getImage();
		topPipeImg = new ImageIcon(getClass().getResource("sprites/toppipe.png")).getImage();
		bottomPipeImg = new ImageIcon(getClass().getResource("sprites/bottompipe.png")).getImage();
		gameOverImg = new ImageIcon(getClass().getResource("sprites/gameover.png")).getImage();
		startButtonImg = new ImageIcon(getClass().getResource("sprites/startbutton.png")).getImage();

		// load bird img
		bird = new Bird(birdImg);

		// load pipes
		pipes = new ArrayList<Pipe>();
		pipeTimer = new Timer(1200, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!justLaunched && !gameLost) {
					placePipes();
				}
			}
		});
		pipeTimer.start();

		// load buttons
		startButton = new Button(startButtonImg);


		// setup button dimensions
		startButton.x = 152;
		startButton.y = 190;
		startButton.width = 56;
		startButton.height = 38;


		// game loop timer
		gameLoop = new Timer(1000/60, this);
		gameLoop.start();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		// draw bg
		g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

		// draw pipes
		for(int i = 0; i < pipes.size(); i++) {
			Pipe pipe = pipes.get(i);
			g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
		}
		
		// draw bird
		g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

		// draw start button on launch
		if(justLaunched) {
			g.drawImage(startButtonImg, 152, 190, 56, 38, null);
		}
	}

	public void moveFunction() {
		// bird movement
		velocityY += fallSpeed;
		velocityY = Math.min(velocityY, 16); // terminal velocity of 16 pixels per frame (?)
		bird.y += velocityY;
		bird.y = Math.max(bird.y, 0); // bird wont go past the vertical limit

		// pipe movement
		for (int i = 0; i < pipes.size(); i++) {
			Pipe pipe = pipes.get(i);
			pipe.x += velocityX;
			
			//collision checker
			if (pipe.top) {
				if (((bird.x + 32) >= pipe.x && bird.x < (pipe.x + 64)) && (bird.y < (pipe.y + 512))) {
					gameLost = true;
				}
			} else {
				if (((bird.x + 32) >= pipe.x && bird.x < (pipe.x + 64)) && ((bird.y + 24) > pipe.y)) {
					gameLost = true;
				}
			}
		}
	}


	public void placePipes() {
		// System.out.println("made pipes");
		int randomPipeY = random.nextInt(366);

		Pipe topPipe = new Pipe(topPipeImg);
		topPipe.y = randomPipeY - 465; // -465 to -100 y level
		topPipe.top = true; // set top status to true since it is a top pipe, not a bottom pipe. this is only for collision check
		pipes.add(topPipe);

		Pipe bottomPipe = new Pipe(bottomPipeImg);
		bottomPipe.y = 125 + topPipe.y + 512; // 125 pixel gap between top and bottom pipe
		pipes.add(bottomPipe);
	}


	// game start function
	public void gameStartLoop() {
		if (gameFrame % 10 == 0) {
			moveState = true;
			startFrameCounter++;
		} else {
			moveState = false;
		}

		if (moveState) {
			switch (startFrameCounter) {
				case 1 -> bird.y -= 9;
				case 4 -> bird.y += 9;
				case 6 -> bird.y += 9;
				case 9 -> bird.y -= 9;
			}
		}

		if (startFrameCounter == 10) {
			startFrameCounter = 0;
		}
	}


	// game lost function
	public void gameLostLoop() {
		velocityY += fallSpeed;
		velocityY = Math.min(velocityY, 12);
		bird.y += velocityY;
		bird.y = Math.min(bird.y, 550);
	}


	// play again function
	public void playAgain() {

	}

	public boolean isInsideButton(int x, int y, Button buttonName) {
		return (x >= buttonName.x && x <= buttonName.x + buttonName.width && y >= buttonName.y && y <= buttonName.y + buttonName.height);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// System.out.println("game frame: " + gameFrame);
		// game state check
		if (justLaunched) {
			// "game just started" layout
			gameStartLoop();
		} else {
			// game is in play
			if (bird.y >= 550 || gameLost) {
				// losing case, run game lost layout
				gameLostLoop();
			} else {
				moveFunction();
			}
		}

		if (gameFrame == 60) {
			gameFrame = 1;
		} else {
			gameFrame++;
		}
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!gameLost && !justLaunched) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE) {
				velocityY = -10;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}


	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (justLaunched) {	
			if (isInsideButton(e.getX(), e.getY(), startButton)) {
				justLaunched = false;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
