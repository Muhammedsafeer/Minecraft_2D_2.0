package main;

public class GameLoop implements Runnable {

    GamePanel gp;

    Thread gameThread;

    int FPS = 60;

    public GameLoop(GamePanel gp) {
        this.gp = gp;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
        System.out.println("Game loop started");
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if (delta >= 1) {
                // 1 UPDATE: update information such as character position, health, etc.
                gp.update();
                // 2 DRAW: draw the updated information to the screen
                gp.repaint();

                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }

}
