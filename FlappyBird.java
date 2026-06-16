import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class FlappyBird extends JPanel implements ActionListener, KeyListener {
	int boardWidth = 360;
	int boardHeight = 640;

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

	//game logic
	Bird bird;
	int velocityY = 0;
	int fallSpeed = 1;

		// losing condition variables
	int topLimit = 34;
	int bottomLimit = 340;

	Timer gameLoop;

	
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
	}

	public void moveFunction() {
		velocityY += fallSpeed;
		velocityY = Math.min(velocityY, 10);
		bird.y += velocityY;
		bird.y = Math.max(bird.y, 0);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// bird movement
		moveFunction();

		// losing condition checker
		if (bird.y == topLimit || bird.y == bottomLimit) {

		}

		repaint();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			velocityY = -12;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}


	@Override
	public void keyReleased(KeyEvent e) {
	}
}
