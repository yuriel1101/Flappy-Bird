import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class FlappyBird extends JPanel {
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


	
	FlappyBird() {
		setPreferredSize(new Dimension(boardWidth, boardHeight));
		backgroundImg = new ImageIcon(getClass().getResource("flappybirdbg.png")).getImage();
		birdImg = new ImageIcon(getClass().getResource("flappybird.png")).getImage();
		topPipeImg = new ImageIcon(getClass().getResource("toppipe.png")).getImage();
		bottomPipeImg = new ImageIcon(getClass().getResource("bottompipe.png")).getImage();

		bird = new Bird(birdImg);
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
}
