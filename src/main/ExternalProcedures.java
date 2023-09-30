package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.util.HashMap;
import java.util.Map;

public class ExternalProcedures {

    public static void print(long value){
        System.out.println(value);
    }

    public static void print_float(double value){
        System.out.println(value);
    }

    public static double _float(long value){
        return (double) value;
    }

    public static double time_seconds(){
        return System.nanoTime() / 1e9;
    }

    public static long _int(double value){
        return (long) value;
    }

    static JFrame frame;
    static Canvas canvas;
    static Graphics2D graphics;
    static BufferStrategy buffer;
    static final Object input_lock = new Object();
    static Map<Integer, Boolean> key_pressed = new HashMap<>();

    public static void open_window(long width, long height){
        frame = new JFrame();
        frame.setSize((int)width, (int)height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        canvas = new Canvas();
        frame.add(canvas);
        frame.setVisible(true);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                synchronized (input_lock){
                    key_pressed.put(e.getKeyCode(), true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                synchronized (input_lock){
                    key_pressed.put(e.getKeyCode(), false);
                }
            }
        });
    }

    public static void frame_begin(){
        if(canvas.getBufferStrategy() == null){
            canvas.createBufferStrategy(3);
        }
        else{
            buffer = canvas.getBufferStrategy();
            graphics = (Graphics2D) buffer.getDrawGraphics();
        }
    }

    public static void clear_screen(){
        if(graphics == null)return;
        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, frame.getWidth(), frame.getHeight());
    }

    public static void draw(){
        if(buffer == null || graphics == null)return;
        buffer.show();
        graphics.dispose();
    }

    public static void fill_rect(long x, long y, long width, long height){
        if(graphics == null)return;
        graphics.setColor(Color.BLUE);
        graphics.fillRect((int)x, (int)y, (int)width, (int)height);
    }

    public static long key_pressed(long key){
        synchronized (input_lock){
            return key_pressed.getOrDefault((int)key, false) ? 1 : 0;
        }
    }

    public static double sqrt(double x){
        return Math.sqrt(x);
    }
}
