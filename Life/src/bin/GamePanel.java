/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * this class will contain all you game logic and graphic<br/>
 * use it for smaller games
 * 
 * @author gbeljajew
 */
public class GamePanel extends JPanel
{
    public static final int width = 1000;
    public static final int high = 800;
    public static final int tileWidth = 10;
    public static final int tileHigh = 10;
    public static final int death = 2;
    public static final int born = 3;
    public static final int normal = 1;
    public static final int empty = 0;
    //---------------------------------------------------
    public boolean runing = false;
    private final int [][] map = new int[97][82];
    private int timer = 0;
   
    
    
    public GamePanel() 
    {
        this.setPreferredSize(new Dimension(width, high));
        this.addMouseListener(new MouseAdapter() 
        {

            @Override
            public void mouseClicked(MouseEvent e) 
            {
                int x = e.getX();
                int y = e.getY();
                if(!runing)
                {
                    set(x/tileWidth+1, y/tileHigh+1);
                }
                
                if(x >= 955 && x <= 995)
                {
                    if(y >= 5 && y<= 25)
                        runing = !runing;
                    
                    if(y >= 35 && y <= 55)
                        clear();
                }
                
            }

            @Override
            public void mouseDragged(MouseEvent e) 
            {
                int x = e.getX();
                int y = e.getY();
                if(!runing)
                {
                    set(x/tileWidth+1, y/tileHigh+1);
                }
            }
            
            
            
        });
        
        this.setLayout(null);
        
       
    }
    
    
    
    
    
    public void init()
    {
        //here comes initiation of your game
    }
    
    public void update()
    {
        
        if(runing)
        {
            timer++;
            
            if(timer==25000)
                timer = 0;
            
            if(timer%20==0)
            {
                setDeath();
                setBorn();
                setNormal();
            }
        }
        
        
    }

    @Override
    public void paint(Graphics g1) 
    {
        Graphics2D g = (Graphics2D)g1;
        g.clearRect(0, 0, width, high);
        
        for(int i = 0; i<=width-50;i+=tileWidth)
            g.drawLine(i, 0, i, high);
        
        for(int i = 0; i<=high;i+=tileHigh)
            g.drawLine(0, i, width-51, i);
        
        
        for(int x = 1; x<map.length-1;x++)
            for(int y = 1; y<map[x].length-1;y++)
                if(map[x][y]>0)
                    g.fillRect((x-1)*tileWidth, (y-1)*tileHigh, tileWidth, tileHigh);
        
        g.draw3DRect(955, 5, 40, 20, true);
        g.draw3DRect(955, 35, 40, 20, true);
        
        if(runing)
        {
            Color color = g.getColor();
            g.setColor(Color.red);
            g.fillRect(955, 5, 40, 20);
            g.setColor(color);
        }
        
        g.drawString("S", 970, 20);
        g.drawString("C", 970, 50);
        
        
        
    }
    
    /**
     * 
     * sets and resets a point on map
     * 
     * @param x
     * @param y 
     */
    private void set(int x, int y)
    {
        if(x<1 || x>=map.length-1 || y<1 || y>=map[x].length-1)return;
        
        if(map[x][y] != empty)
            map[x][y]=empty;
        else
            map[x][y]=normal;
        
        setBorder();
    }
    
    /**
     * this methode wraps map to tor
     */
    private void setBorder()
    {
        for(int i = 1;i<map.length-1;i++)
        {
            map[i][0]=map[i][map[i].length-2];
            map[i][map[i].length-1] = map[i][1];
        }
        
        for(int i = 1; i< map[0].length;i++)
        {
            map[0][i] = map[map.length-2][i];
            map[map.length-1][i] = map[1][i];
        }
        
        map[0][0] = map[map.length-2][map[0].length-2];
        map[0][map[0].length-1] = map[map.length-2][1];
        map[map.length-1][0] = map[1][map[0].length-2];
        map[map.length-1][map[0].length-1] = map[1][1];
        
    }
    
    /**
     * resets map
     */
    private void clear()
    {
        for(int x = 0; x<map.length; x++)
            for(int y =0; y< map[x].length; y++)
                map[x][y]=empty;
    }
    
    /**
     * counts all points in a 3x3 grid around point x,y.<br/>
     * will not count points, that will be born next turn.<br/>
     * it counts central point too so if there was a point on x,y then use count()-1
     * 
     * @param x
     * @param y
     * @return number of points. <br/>or -1 if x,y outside map, 
     */
    private int count(int x, int y)
    {
        if(x<1 || x>=map.length-1 || y<1 || y>=map[x].length-1)return -1;
        
        int s = 0;
        
        for(int i = x-1; i<=x+1; i++)
            for(int j = y-1; j<=y+1;j++)
                if(map[i][j] == normal || map[i][j] == death)
                    s++;
        
        return s;
    }
    
    /**
     * goes trough map and sets points, that will die next turn.
     */
    private void setDeath()
    {
        for(int x = 1; x<map.length-1;x++)
            for(int y = 1; y<map[x].length;y++)
                if(map[x][y]==normal)
                {
                    int count = count(x,y)-1;
                    if(count<2 || count >3)
                        map[x][y]=death;
                }
    }
    
    /**
     * goes trough map and sets points, that will be born next turn.
     */
    private void setBorn()
    {
        for(int x = 1; x<map.length-1;x++)
            for(int y = 1; y<map[x].length;y++)
                if(map[x][y]==empty)
                {
                    int count = count(x,y);
                    if(count==3)
                        map[x][y]=born;
                }
    }
    
    /**
     * removes all points, that are set to die and sets all born points to normal
     */
    private void setNormal()
    {
        for(int x = 1; x<map.length-1;x++)
            for(int y = 1; y<map[x].length;y++)
            {
                if(map[x][y]==death)
                    map[x][y]=empty;
                
                if(map[x][y]==born)
                    map[x][y]=normal;
            }
        
        setBorder();
    }
}
