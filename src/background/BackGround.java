package background;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class BackGround {
    private BufferedImage wholeImg;
    private BufferedImage cutImg;
    private int pixelPerFrame;
    private int offset = 0;

    public BackGround(int speed){
        pixelPerFrame = speed;
        importImg();
    }

    private void importImg(){
        InputStream is = getClass().getResourceAsStream("/32x9_background.jpg");

        try {
            wholeImg = ImageIO.read(is);
            scaleImage();
        }
        catch (IOException e) {e.printStackTrace();}
    }

    public BufferedImage scaleImage() {
        Image scaledImage = wholeImg.getScaledInstance(2560, 720, Image.SCALE_SMOOTH);
        cutImg = new BufferedImage(2560, 720, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = cutImg.createGraphics();
        g2d.drawImage(scaledImage, 0, 0, null);
        g2d.dispose();
        return cutImg;
    }

    public BufferedImage nextImg() {
        if(offset == 1280 || offset + pixelPerFrame == 0){
            pixelPerFrame = -pixelPerFrame;
        }
        offset += pixelPerFrame;
        BufferedImage displayImage = cutImg.getSubimage(offset,0,1280,720);
        return displayImage;
    }
}
