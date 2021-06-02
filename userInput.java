import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

public class userInput {
    static Robot rbt;

    public static void main(String[] args) throws InterruptedException {

            try {
                rbt = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }
for(int i= 0; i < 10; i++)
{
	lauktiMS(600);
	sauti();
}}
    static void kaire(int x) throws InterruptedException {
        rbt.keyPress(KeyEvent.VK_LEFT);
        TimeUnit.MILLISECONDS.sleep(x);
        rbt.keyRelease(KeyEvent.VK_LEFT);
    }
    static void desine(int x) throws InterruptedException {
        rbt.keyPress(KeyEvent.VK_RIGHT);
        TimeUnit.MILLISECONDS.sleep(x);
        rbt.keyRelease(KeyEvent.VK_RIGHT);
    }
    static void sokti() throws InterruptedException {
        rbt.keyPress(KeyEvent.VK_UP);
        TimeUnit.MILLISECONDS.sleep(20);
        rbt.keyRelease(KeyEvent.VK_UP);
    }
    static void soktiDesinen(int x) throws InterruptedException {
        rbt.keyPress(KeyEvent.VK_RIGHT);
        TimeUnit.MILLISECONDS.sleep(x/2);
        rbt.keyRelease(KeyEvent.VK_RIGHT);
        rbt.keyPress(KeyEvent.VK_UP);
        TimeUnit.MILLISECONDS.sleep(20);
        rbt.keyRelease(KeyEvent.VK_UP);
        rbt.keyPress(KeyEvent.VK_RIGHT);
        TimeUnit.MILLISECONDS.sleep(x/2);
        rbt.keyRelease(KeyEvent.VK_RIGHT);
    }
    static void soktiKairen(int x) throws InterruptedException {
        rbt.keyPress(KeyEvent.VK_LEFT);
        TimeUnit.MILLISECONDS.sleep(x/2);
        rbt.keyPress(KeyEvent.VK_UP);
        TimeUnit.MILLISECONDS.sleep(20);
        rbt.keyRelease(KeyEvent.VK_UP);
        TimeUnit.MILLISECONDS.sleep(x/2);
        rbt.keyRelease(KeyEvent.VK_LEFT);
    }
    static void sauti() throws InterruptedException {
        rbt.keyPress(KeyEvent.VK_SPACE);
        TimeUnit.MILLISECONDS.sleep(20);
        rbt.keyRelease(KeyEvent.VK_SPACE);
    }
    static void pereitiPirma() throws InterruptedException {
        laukti(2);
        soktiDesinen(400);
        laukti(2);
        sokti();
        desine(250);
        laukti(2);
        soktiDesinen(450);
        laukti(2);
        soktiDesinen(400);
        laukti(2);
        sokti();
        desine(1500);
        laukti(2);
        sauti();
        desine(2250);
        sauti();
        sokti();
        kaire(100);
        desine(200);
        laukti(2);
        sokti();
        desine(1000);
        laukti(2);
        rbt.keyPress(KeyEvent.VK_X);
        TimeUnit.MILLISECONDS.sleep(20);
        rbt.keyRelease(KeyEvent.VK_X);
    }
    static void pereitiAntra() throws InterruptedException {
        laukti(1);
        sokti();
        lauktiMS(40);
        sauti();
        laukti(2);
        kaire(200);
        laukti(1);
        desine(200);
        sauti();
        sokti();
        desine(2000);
        sauti();
        soktiDesinen(1000);
        soktiDesinen(800);
        desine(3000);
        sokti();
        lauktiMS(300);
        desine(200);
        laukti(2);
        soktiKairen(500);
        kaire(100);
        laukti(2);
        desine(20);
        sokti();
        lauktiMS(300);
        sauti();
        laukti(2);
        desine(50);
        sokti();
        desine(2000);
        laukti(2);
        rbt.keyPress(KeyEvent.VK_X);
        TimeUnit.MILLISECONDS.sleep(20);
        rbt.keyRelease(KeyEvent.VK_X);
    }
    static void pereitiTrecia() throws InterruptedException {
        laukti(1);
        desine(100);
        laukti(1);
        sokti();
        lauktiMS(300);
        desine(200);
        laukti(1);
        sokti();
        lauktiMS(300);
        desine(200);
        laukti(1);
        sokti();
        lauktiMS(300);
        sauti();
        laukti(2);
        sokti();
        lauktiMS(300);
        desine(1000);
        laukti(2);
        kaire(500);
        laukti(1);
        desine(20);
        sauti();
        lauktiMS(500);
        desine(2000);
        sauti();
        sokti();
        desine(2000);
        laukti(1);
        sauti();
        sokti();
        lauktiMS(350);
        desine(150);
        laukti(1);
        sokti();
        desine(300);
        sauti();
        desine(600);
        lauktiMS(500);
        sokti();
        desine(500);
        rbt.keyPress(KeyEvent.VK_X);
        TimeUnit.MILLISECONDS.sleep(20);
        rbt.keyRelease(KeyEvent.VK_X);
    }
    static void pereitiVisus() throws InterruptedException {
        pereitiPirma();
        pereitiAntra();
        pereitiTrecia();
    }
    static void laukti(int x) throws InterruptedException {
        TimeUnit.SECONDS.sleep(x);
    }
    static void lauktiMS(int x) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep(x);
    }
}
