import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener, KeyListener {
	int boardWidth = 360;
	int boardHeight = 640;
	Random random = new Random();

	// Images
	Image backgroundImg;
	Image birdImg;
	Image topPipeImg;
	Image bottomPipeImg;

	// Bird stuff
	int birdX = boardWidth/7;
	int birdY = boardHeight/2;
	int birdWidth = 34;
	int birdHeight = 24;

	class Bird {
		int x = birdX;
		int y = birdY;
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
		int width = pipeWidth;
		int height = pipeHeight;
		Image img;
		boolean passed = false;

		Pipe(Image img) {
			this.img = img;
		}
	}




	//game logic
	Bird bird;
	int velocityX = -4;
	int velocityY = 0;
	int fallSpeed = 1;

	boolean justLaunched = false;
	boolean gameEnded = false;

	Timer gameLoop;
	Timer pipeTimer;

	ArrayList<Pipe> pipes;


	
	FlappyBird() {
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		setFocusable(true);
		addKeyListener(this);

		// load images
		backgroundImg = new ImageIcon(getClass().getResource("flappybirdbg.png")).getImage();
		birdImg = new ImageIcon(getClass().getResource("flappybird.png")).getImage();
		topPipeImg = new ImageIcon(getClass().getResource("toppipe.png")).getImage();
		bottomPipeImg = new ImageIcon(getClass().getResource("bottompipe.png")).getImage();

		// load bird img
		bird = new Bird(birdImg);

		// load pipes
		pipes = new ArrayList<Pipe>();
		pipeTimer = new Timer(1500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				placePipes();
			}
		});
		pipeTimer.start();


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

		// draw bird
		g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

		// draw pipes
		for(int i = 0; i < pipes.size(); i++) {
			Pipe pipe = pipes.get(i);
			g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
		}
	}

	public void moveFunction() {
		// bird movement
		velocityY += fallSpeed;
		velocityY = Math.min(velocityY, 16);
		bird.y += velocityY;
		bird.y = Math.max(bird.y, 0);

		// pipe movement
		for (int i = 0; i < pipes.size(); i++) {
			Pipe pipe = pipes.get(i);
			pipe.x += velocityX;

		}
	}


	public void placePipes() {
		//-450 max
		// int randomPipeY = 

		Pipe topPipe = new Pipe(topPipeImg);
		topPipe.y = -450;
		pipes.add(topPipe);

		Pipe bottomPipe = new Pipe(bottomPipeImg);
		bottomPipe.y = 125 + topPipe.y + 512;
		pipes.add(bottomPipe);


	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// game state check
		if (justLaunched) {
			// "game just started" layout
		} else {
			// game is in play
			if (bird.y >= 550) {
				// losing case
				gameEnded = true;
			} else {
				moveFunction();
			}
		}

		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			velocityY = -10;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}


	@Override
	public void keyReleased(KeyEvent e) {
	}
}
