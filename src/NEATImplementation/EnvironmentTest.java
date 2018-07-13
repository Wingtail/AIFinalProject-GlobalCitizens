package NEATImplementation;
//package NEATImplementation;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import NEAT.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
class Agent extends JComponent{
   
   //Agent Parameters
   double proxLength = 300;
   double speed = 1;
   double angle = Math.PI*3/2;
   int size = 100;
   double x = 100, y = 100;
   double x1, x2, x3, x4, y1, y2, y3, y4, xp0, xp1, yp0, yp1;
   double xPos, yPos;
   double p0=0,p1=0;
   long currentTime =  System.currentTimeMillis();
   double shortest = -1;

    int alpha = 127;
    Color c = new Color((int)(Math.random()*200),(int)(Math.random()*200),(int)(Math.random()*200),alpha);

    boolean isAlive = true;

   double timeSpawned = 0.0;
   double timeAlive = 0.0;

   int currentBushIndex = 0;

   double timeStayedinPosStart = 0.0;
   double timeStayedinPosEnd = 0.0;

   Organism organism;
   
   //Hamster Parameters
   int leftSpeed = 60;
   int rightSpeed = 60;
   
   //Frame Parameters
   int width = 1600;
   int height = 900;
   
   //Hamster Kinimatics Setup
   boolean updateAngle = true;
   double deltaAngle = 0;
   
   public Agent(int x, int y) {
      this.x = x;
      this.y = -y;
      setSize(width, height);

      System.out.println("Organism not initialized!");
   }

   public Agent(int x, int y, Organism organism)
   {
       this.x = x;
       this.y = -y;
       this.organism = organism;
       setSize(width,height);
   }
   
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d=(Graphics2D)g;
      
        double pi4 = (3.1415 / 4.0);
        
        double a1 = angle + pi4;
        double a2 = angle + 3*pi4;
        double a3 = angle + 5*pi4;
        double a4 = angle + 7*pi4;
   
        x1 = 20*Math.sqrt(2) * Math.cos(a1) + x;
        x2 = 20*Math.sqrt(2) * Math.cos(a2) + x;
        x3 = 20*Math.sqrt(2) * Math.cos(a3) + x;       
        x4 = 20*Math.sqrt(2) * Math.cos(a4) + x;
        
        xp0 = p0 * Math.cos(angle + Math.PI/2.0) + x1;
        xp1 = p1 * Math.cos(angle + Math.PI/2.0) + x2;
        
        y1 = 20*Math.sqrt(2) * Math.sin(a1) - y;
        y2 = 20*Math.sqrt(2) * Math.sin(a2) - y;
        y3 = 20*Math.sqrt(2) * Math.sin(a3) - y;
        y4 = 20*Math.sqrt(2) * Math.sin(a4) - y;

        yp0 = p0 * Math.sin(angle + Math.PI/2.0) + y1;
        yp1 = p1 * Math.sin(angle + Math.PI/2.0) + y2;
        
        Polygon robot = new Polygon();
        
        robot.addPoint((int)x1, (int)y1);
        robot.addPoint((int)x2, (int)y2);
        robot.addPoint((int)x3, (int)y3);
        robot.addPoint((int)x4, (int)y4);
   
        g.setColor(c);
        g2d.fillPolygon(robot);
        
        g.setColor(Color.red);
        g2d.fillOval((int)x1-5, (int)y1-5, 10, 10);
        g2d.fillOval((int)x2-5, (int)y2-5, 10, 10);
        
        g.drawLine((int)x1, (int)y1, (int)xp0, (int)yp0);
        g.drawLine((int)x2, (int)y2, (int)xp1, (int)yp1);
        
        //System.out.println((int)((20*Math.sqrt(2)-2) * Math.sin(angle) + (int)((x1 + x3)/2.0)-5) + " - " + (int)((20*Math.sqrt(2)-2) * Math.cos(angle) - ((y1 + y3)/2.0)-5));
      
   }
   public void update() {
       long time = System.currentTimeMillis();
      double deltaT = currentTime/1000.0 - time/1000.0;
      timeAlive = time;

      computeSpeed();
      
      if(angle > 2*Math.PI) {
         angle -= 2*Math.PI;
      }
      if(angle < 0) {
         angle += 2*Math.PI;
      }
      double newPos = (leftSpeed*deltaT + rightSpeed*deltaT)/2;
      double newAngle = angle + (leftSpeed-rightSpeed)*deltaT/35;
      double newX = x + newPos * Math.sin(angle) * 1.25;
      double newY = y + newPos * Math.cos(angle) * 1.25;
      double[][] myObs = new double[1][4];
      myObs[0][0] = 0;
      myObs[0][1] = 0;
      myObs[0][2] = 0;
      myObs[0][3] = 0;
      //if isCollided()
      x = newX;
      y = newY;
      angle = newAngle;
      currentTime = time;

      //System.out.println(x + " - " + y);
   }

   public void computeFitness(int bushFire, double bushMax)
   {

       this.organism.fitness = (this.timeAlive - this.timeSpawned)/1000 + (bushMax - bushFire); //Add bushfire also
   }

    public void computeSpeed()
    {
        System.out.println(p0+" "+p1);
        this.organism.getNetwork().ForwardProp(this.p0,this.p1);
        double[] output = this.organism.getNetwork().output();

        System.out.println("Output: "+output[0]+" - "+output[1]);

        leftSpeed = (int)(output[0]*100+30);
        System.out.println(leftSpeed);
        rightSpeed = (int)(output[1]*100+30);
    }
    public double getProx(int side, Object obsList []) {
        Point intersect = new Point();

        if (side == 0) {
            intersect = getSensor(0,obsList);
//	       System.out.println("POINT: ("+intersect.x+","+intersect.y+")");
            double p0 = Math.abs(distance(intersect.x,intersect.y,x1,y1));
//	       System.out.println("p0: " + p0);
            if(p0 < proxLength) {
                this.p0 = p0;
                return p0;
            }
        } else {
            intersect = getSensor(1,obsList);
//	       System.out.println("POINT: ("+intersect.x+","+intersect.y+")");
            double p1 = Math.abs(distance(intersect.x,intersect.y,x2,y2));
//	       System.out.println("p1: " + p1);
            if(p1 < proxLength) {

                this.p1 = p1;
                return p1;
            }
        }
        this.p0 = 0;
        this.p1 = 0;
        return 0;
    }
    public Point getSensor(int side, Object obsList []) {
        //ANGLE 0 is DOWN
        ArrayList<Point> pList = new ArrayList<>();


        //Left
        if (side == 0) {
            xPos = x1;
            yPos = y1;
        } else {
            xPos = x2;
            yPos = y2;
        }
        double newAngle = ((angle+2*Math.PI) % (2*Math.PI));
        //Get equation of line
        //y = m*(x-xPos)+yPos
        //x =(y-yPos)/m + xPos

//	    System.out.println("ANGLE: " + String.valueOf(newAngle));

        double m;
        //Four quadrants
        if (newAngle >= 0 && newAngle < Math.PI/2) {
            //3rd Quadrant
//	       System.out.println("IN QUADRENT 3");
            m = -1/Math.tan(newAngle);
//	       System.out.println("M: " + String.valueOf(m));

            for (Object obs : obsList ) {
                if (obs.minX > xPos || obs.maxY < yPos) {
//	             System.out.println("CONTINUE FROM QUADRANT 3");
                    continue;
                }

                //if in 1st quad flip slope
                double nm;
                if (obs.maxY <= yPos && obs.minX >= xPos){
                    nm = -m;
                } else {
                    nm = m;
                }

                //Sides: Bottom and Left
                //Right
                double possY = nm * (obs.maxX - xPos) + yPos;
                if (possY >= obs.minY && possY <= obs.maxY) {
//	             System.out.print("INTERSECTING RIGHT 1 "); //Here
                    pList.add(new Point((int) obs.maxX, (int) possY));
                    continue;

                }
                //Top side
                double possX = ((obs.minY - yPos) / nm) + xPos;
                if (possX >= obs.minX && possX <= obs.maxX) {
//	             System.out.print("INTERSECTING TOP 2 "); //Here
                    pList.add(new Point((int) possX, (int) obs.minY));
                    continue;
                }
            }


        } else if (newAngle >= Math.PI/2 && newAngle < Math.PI) {
            //2nd Quadrant
//	       System.out.println("IN QUADRENT 2");
            m = -1/Math.tan(newAngle);
//	       System.out.println("M: " + String.valueOf(m));
            for (Object obs : obsList ) {
                //Sides: Bottom and Left
                //if in 4th quad flip slope
                if (obs.maxX < xPos || obs.maxY < yPos) {
//	             System.out.println("CONTINUE FROM QUADRANT 2");
                    continue;
                }

                double nm;
                if (obs.minY >= yPos && obs.minX >= xPos){
                    nm = -m;
                } else {
                    nm = m;
                }

                //Right
                double possY = nm * (obs.maxX - xPos) + yPos;
                if (possY >= obs.minY && possY <= obs.maxY) {
//	             System.out.print("INTERSECTING RIGHT 3 ");
                    pList.add(new Point((int) obs.maxX, (int) possY));
                    continue;
                }
                //Bottom side
                double possX = ((obs.maxY - yPos) / nm) + xPos;
                if (possX >= obs.minX && possX <= obs.maxX) {
//	             System.out.print("INTERSECTING BOTTOM 4 ");
                    pList.add(new Point((int) possX, (int) obs.maxY));
                    continue;
                }
            }

        } else if (newAngle >= Math.PI && newAngle < 3*Math.PI/2) {
            //1st Quadrant
//	       System.out.println("IN QUADRENT 1");
            m = -1/Math.tan(newAngle);
//	       System.out.println("M: " + String.valueOf(m));
            for (Object obs : obsList ) {
                //Sides: Bottom and Left
                //if in 3rd quad flip slope
                if (obs.maxX < xPos || obs.minY > yPos) {
//	             System.out.println("CONTINUE FROM QUADRANT 1");
                    continue;
                }

                double nm;
                if (obs.minY >= yPos && obs.maxX <= xPos){
                    nm = -m;
                } else {
                    nm = m;
                }

                //Left side
                double possY = nm*(obs.minX-xPos) + yPos;
                if (possY >= obs.minY && possY <= obs.maxY) {
//	             System.out.print("INTERSECTING LEFT 5 "); //HERE
                    pList.add(new Point((int)obs.minX, (int)possY));
                    continue;
                }
                //Bottom side
                double possX = ((obs.maxY-yPos)/nm) + xPos;
                if (possX >= obs.minX && possX <= obs.maxX) {
//	             System.out.print("INTERSECTING BOTTOM 6 ");
                    pList.add(new Point((int)possX, (int)obs.maxY));
                    continue;
                }
            }

        } else if (3*newAngle >= Math.PI/2 && newAngle <= 2*Math.PI) {
            //4th Quadrant
//	       System.out.println("IN QUADRENT 4");
            m = -1/Math.tan(newAngle);
//	       System.out.println("M: " + String.valueOf(m));
            for (Object obs : obsList ) {
                //Sides: Top and Left

                if (obs.maxX < xPos || obs.maxY < yPos) {
//	             System.out.println("CONTINUE FROM QUADRANT 4");
                    continue;
                }
                //if in 2nd quadrent
                double nm;
                if (obs.maxY <= yPos && obs.maxX <= xPos){
                    nm = -m;
                } else {
                    nm = m;
                }

                //Left side
                double possY = nm*(obs.minX-xPos) + yPos;
                if (possY >= obs.minY && possY <= obs.maxY) {
//	             System.out.println("INTERSECTING LEFT 7 ");
//	             System.out.println("minX: " + obs.minX);
//	             System.out.println("possY: " + possY);
//	             System.out.println("POINT INSIDE: ("+p.x+","+p.y+")");
                    pList.add(new Point((int)obs.minX, (int)possY));
                    continue;

                }
                //Top side
                double possX = ((obs.minY-yPos)/nm) + xPos;
                if (possX >= obs.minX && possX <= obs.maxX) {
//	             System.out.print("INTERSECTING TOP 8 ");
                    pList.add(new Point((int)possX, (int)obs.minY));
                    continue;
                }
            }

        } else {
            System.out.println("ERROR: ANGLE NOT IN RANGE FOR PROX???");
            System.out.println("THE ANGLE IS " + String.valueOf(newAngle));
        }
        xPos = (x1+x3)/2;
        yPos = (y1+y3)/2;
        if (pList.size() == 0) {
            return new Point();
        } else {
            double minDist = distance(xPos,yPos, pList.get(0).x,pList.get(0).y);
            Point minP = pList.get(0);

            for (Point p : pList) {
                double dist = distance(xPos,yPos, p.x,p.y);
                if (dist < minDist) {
                    minDist = dist;
                    minP = p;
                }
            }
            return minP;
        }
    }
   public double distance(double x1, double y1, double x2, double y2) {
      return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
   }
   public boolean isFilled(ArrayList <Double> list) {
      
      for(int k = 0; k < list.size(); k ++) {
       if(list.get(k) != -10.0) {
          return true;
       }
     }
      return false;
   }
    public boolean isCollided(double[][] obs) {
        boolean ret = false;

        int NUM_POINTS = 10;
        double[][] fullPoints = new double[NUM_POINTS*4][2];

        double dx12 = (double)(x1-x2)/(double)(NUM_POINTS+1);
        double dx23 = (double)(x2-x3)/(double)(NUM_POINTS+1);
        double dx34 = (double)(x3-x4)/(double)(NUM_POINTS+1);
        double dx41 = (double)(x4-x1)/(double)(NUM_POINTS+1);
        double dy12 = (double)(y1-y2)/(double)(NUM_POINTS+1);
        double dy23 = (double)(y2-y3)/(double)(NUM_POINTS+1);
        double dy34 = (double)(y3-y4)/(double)(NUM_POINTS+1);
        double dy41 = (double)(y4-y1)/(double)(NUM_POINTS+1);

        for (int i = 1; i < NUM_POINTS; i++) {
            fullPoints[i][0] = dx12*i+x2;
            fullPoints[i+NUM_POINTS][0] = dx23*i+x3;
            fullPoints[i+2*NUM_POINTS][0] = dx34*i+x4;
            fullPoints[i+3*NUM_POINTS][0] = dx41*i+x1;

            fullPoints[i][1] = dy12*i+y2;
            fullPoints[i+NUM_POINTS][1] = dy23*i+y3;
            fullPoints[i+2*NUM_POINTS][1] = dy34*i+y4;
            fullPoints[i+3*NUM_POINTS][1] = dy41*i+y1;
        }


        fullPoints[0][0] = fullPoints[1][0];
        fullPoints[0][1] = fullPoints[1][1];
        for (int i = 10; i<NUM_POINTS*4; i+=10) {
            fullPoints[i][0] = fullPoints[i-1][0];
            fullPoints[i][1] = fullPoints[i-1][1];
        }
//	    System.out.println("\n###########################\nPRINTING FULL POINTS\n########################");
//	    for (double[] point : fullPoints) {
//	       System.out.println("("+ String.valueOf(point[0]) + "," + String.valueOf(point[1]) + ")");
//	    }



        collisionLoop:
        for (double[] object : obs) {
            double minX = object[0];
            double minY = object[1];
            double maxX = object[2];
            double maxY = object[3];
            if (object[2] < object[0]) {
                minX = object[2];
                maxX = object[0];
            }
            if (object[3] < object[1]) {
                minY = object[3];
                maxY = object[1];
            }
//	       System.out.println(minX + " - " + minY);
//	       System.out.println(maxX + " - " + maxY);
            double dist = Math.pow(((this.x1-this.x3)/2.0 - (minX+maxX)/2.0),2) + Math.pow(((this.y1-this.y3)/2.0 - (minY+maxY)/2.0),2);
            //Max dist to consider squared
            if (dist < 25000000) {

                double[][] points = new double[4][2];
                points[0][0] = x1;
                points[0][1] = y1;
                points[1][0] = x2;
                points[1][1] = y2;
                points[2][0] = x3;
                points[2][1] = y3;
                points[3][0] = x4;
                points[3][1] = y4;

//	          System.out.println("\n###########################\nPRINTING POINTS\n########################");
//	          for (double[] point : points) {
//	             System.out.println("("+ point[0] + "," + point[1] + ")");
//	          }

                for (double[] point : points) {
                    if (point[0] == 0.0 && point[1] == 0.0){
                        continue;
                    }

                    if (point[0] >= minX && point[0] <= maxX && point[1] >= minY && point[1] <= maxY) {
                        ret = true;
//	                System.out.println("Point Collision 1");
//	                System.out.println("COLLIDING POINT IS (" + String.valueOf(point[0]) + "," + String.valueOf(point[1]) + ")");
                        break collisionLoop;
                    }
                }

                for (double[] point : fullPoints) {
                    if (point[0] == 0.0 && point[1] == 0.0){
                        continue;
                    }

                    if (point[0] >= minX && point[0] <= maxX && point[1] >= minY && point[1] <= maxY) {

                        ret = true;
//	                System.out.println("Point Collision 2");
//	                System.out.println("COLLIDING POINT IS (" + String.valueOf(point[0]) + "," + String.valueOf(point[1]) + ")");
                        break collisionLoop;
                    }
                }
            }




        }


        return ret;
    }
   public double calculateAngle(double x1, double y1, double x2, double y2)
   {
       double angle = Math.toDegrees(Math.atan2(x2 - x1, y2 - y1));
       // Keep angle between 0 and 360
       angle = angle + Math.ceil( -angle / 360 ) * 360;
       return angle;
   }
}

class Object extends JComponent{
   int x = 0;
   int y = 0;
   
   double x1, x2, x3, x4, y1, y2, y3, y4;
   boolean vertical;
   int obsWidth = 5;
   int obsHeight = 90;
   
   double facing = 45;
   double speed = 1.5;
   
   int width = 1600;
   int height = 900;

   double minX, maxX, minY, maxY;
   
   Color c = new Color((int)(Math.random()*200),(int)(Math.random()*200),(int)(Math.random()*200));
   public Object(int xPos, int yPos, boolean vertical) {//does work
       this.vertical = vertical;
       setSize(width, height);
       x = xPos + (int)(obsWidth/2.0);
       y = yPos + (int)(obsHeight/2.0);

       
        x1 = x;   
        y1 = y;
        
        if(vertical) {
	        x2 = x + obsWidth;
	        y2 = y + obsHeight;
        }else {

	        x2 = x + obsHeight;
	        y2 = y + obsWidth;
        }

        minX = x1;
        maxX = x2;
        minY = y1;
        maxY = y2;
        
   }
   
   public void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.setColor(c);
      if(vertical) {
          g.fillRect(x, y, obsWidth, obsHeight);
      } else {
          g.fillRect(x, y,obsHeight, obsWidth);
      }
   }
   public void update() {//does not get called
      System.out.print("update function");
      //x += Math.sin(Math.toRadians(facing))*speed;
      //y += Math.cos(Math.toRadians(facing))*speed;
   }
   public double[] getPos() {
      System.out.println(x1 + " - " + y1 + " --------- " + x2 + " - " + y2);//does print
      double corners [] = new double[4];
      
      corners[0] = (double)x1;
      corners[1] = (double)y1;
      corners[2] = (double)x2;
      corners[3] = (double)y2;
      
      return corners;
   }
   public boolean checkCol(Agent a) {
      
      return false;
      
      
   }
   public double distance(double x1, double y1, double x2, double y2) {
      return Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
   }
}




class Maze extends JComponent {
    Agent a;
    ArrayList <Object> obsList = new ArrayList <Object>();

    int bushfire[];
    int randomBoardSize;
    int hDimension;
    int vDimension;

    public Maze(int widthC, int heightC, Agent a) {
        this.a = a;

        ArrayList<ArrayList<Double>> walls = new ArrayList<ArrayList<Double>>();

        //Set to either 2 or 3, 2 if dealing with 4 -by- 4 to 5 -by- 5, 3 if 6 -by- 6 is included
        randomBoardSize = (int)((Math.random()*2) + 4);

        hDimension = 90*randomBoardSize;
        vDimension = hDimension;

        String fileName = randomBoardSize +"x"+ randomBoardSize +"_#";
        //fileName += (int)(Math.random()*1000);
        fileName += (int)(Math.random()*1000);
        //System.out.println(fileName);
        System.out.println("File Name: "+fileName);
        File file = new File("Maps4/" + fileName.substring(0,3) +"/" +fileName + ".txt");
        Scanner scan;
        int bugCounter = 0;
        try {
            scan = new Scanner(file);

            int counterb = 0;
            //boolean connections[] = new boolean[((randomBoardSize-1)*(randomBoardSize)*(randomBoardSize-1)+(randomBoardSize-1))];


            bushfire = new int[(randomBoardSize)*(randomBoardSize)];

            // 1600x900

            double margin = 40;

            //double w_scalar = (1600-margin*2)/(randomBoardSize);
            //double h_scalar = (900-margin*2)/(randomBoardSize);
            //System.out.println(w_scalar);

            for(int k = 0; k < (randomBoardSize+1); k ++) {
                for(int v =0; v < (randomBoardSize+1); v ++) {
                    if((v == randomBoardSize || v == 0) && k < randomBoardSize) {
                        double xPos1 = (90 * (v+1));
                        double yPos1 = (90 * (k));

                        Object obs = new Object((int)xPos1,(int)yPos1,true);
                        a.add(obs);
                        obsList.add(obs);
                    }
                    if((k == randomBoardSize || k == 0) && v < randomBoardSize) {
                        double xPos1 = (90 * (v+1));
                        double yPos1 = (90 * (k));

                        Object obs = new Object((int)xPos1,(int)yPos1,false);
                        a.add(obs);
                        obsList.add(obs);
                    }
                }
            }
            for(int k = 0; k < (randomBoardSize-1); k ++) {
                for(int v =0; v < (randomBoardSize-1); v ++) { //v = vertical
                    //connections[counter] = scan.nextBoolean();
                    double xPos1 = (90 * (v+1));
                    double yPos1 = (90 * (k+1));

                    if(scan.hasNext()) {
                        boolean descision = scan.nextBoolean();
                        if(descision) {
                            Object obs = new Object((int)xPos1,(int)yPos1,true);
                            a.add(obs);
                            obsList.add(obs);
                        }
                        bugCounter++;
                        System.out.println(bugCounter);
                        System.out.println("size: "+(randomBoardSize-1)+"");
                    }


                }
                for(int h =0; h < (randomBoardSize); h ++) { //h = horizontal
                    //connections[counter] = scan.nextBoolean();
                    double xPos1 = (90 * (h+1));
                    double yPos1 = (90 * (k+1));
                    if(scan.hasNext()) {
                        boolean descision = scan.nextBoolean();
                        if(descision) {
                            Object obs = new Object((int)xPos1,(int)yPos1,false);

                            a.add(obs);

                            obsList.add(obs);
                        }
                        bugCounter++;
                        System.out.println(bugCounter);
                    }
                }
                for(int b =0; b < (randomBoardSize); b ++) {
                    bushfire[counterb] = scan.nextInt();
                    counterb ++;
                }
            }
            System.out.println(bugCounter);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
   public void update() {
      repaint();
   }
}





class GUI extends JPanel implements ActionListener {
 private final int DELAY = 0;
 private Timer timer;
 public GUI() {
   timer = new Timer(DELAY, this);
     timer.start();
     setDoubleBuffered(true);
 }
 
 public Timer getTimer() {
     return timer;
 }
 public void paintComponent(Graphics g) {//does not get called
     super.paintComponent(g);
     System.out.print("paintComponent");
   }
 public void actionPerformed(ActionEvent e) {
     repaint();
     revalidate();
 } 
}

class EnvironmentTest extends JFrame {
    NetworkJsonEncoder networkJsonEncoder = new NetworkJsonEncoder();
   boolean toggle = false;
   static int key;
   boolean left = false, right = false;

   int population = 50;

   Agent[] agentList;

   public static GUI panel;

   public NEAT_Toolchain nTool = new NEAT_Toolchain();

 public EnvironmentTest() {
   panel = new GUI();
   addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent e) {
             Timer timer = panel.getTimer();
             networkJsonEncoder.endProcess();
             timer.stop();
         }
     }
     );
   
     setTitle("Environment Testing");
     setSize(1600,900);
     setLocationRelativeTo(null);
     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     setVisible(true);

     nTool.generateInitialPopulation(2,2,population);

     System.out.println("NEAT population generated: "+Population.getPopulationSize());
     agentList = new Agent[population];
     int i;
     int j;

     int counter = 0;
     for(i=0;i<Population.getPopulationSize();i++)
     {
         for(j=0;j<Population.getPopulationElement(i).getSpeciesSize();j++)
         {
             System.out.println("Genome "+counter+": "+Population.getPopulationElement(i).getSpeciesElement(j));
             agentList[counter] = new Agent(135,90,Population.getPopulationElement(i).getSpeciesElement(j));
             agentList[counter].timeSpawned = System.currentTimeMillis();
             System.out.println("Agent : "+agentList[counter]);
             counter++;
         }
     }

     System.out.println("Agent List: "+agentList);
 }

 public static void main(String[] args) {
   EnvironmentTest envi = new EnvironmentTest();

   int timeRemainSpaceThresh = 5;

//     Agent a1 = new Agent(400,400);
//
//     Agent a2 = new Agent(500,500);
//

   Maze m = new Maze(5,5, envi.agentList[0]);
//     Object o = o1;

     envi.add(envi.agentList[0]);
     envi.agentList[0].add(m);
     for(int i = 1; i < envi.agentList.length; i ++) {
         envi.agentList[0].add(envi.agentList[i]);
     }

     double obsPos[][] = new double[m.obsList.size()][4];

     Object obsList[] = new Object[m.obsList.size()];
     for(int i = 0 ; i < m.obsList.size(); i++) {
         obsPos[i] = m.obsList.get(i).getPos();
     }
     for(int i = 0 ; i < m.obsList.size(); i++) {
         obsList[i] = m.obsList.get(i);
     }
     m.update();

   int counter = 0;

   ArrayList<Agent> agents = new ArrayList<Agent>();

   for(Agent agent:envi.agentList)
   {
       agents.add(agent);
   }
   
   while(true) {
      //Repaints GUI, triggering refresh of all jComp's
      envi.getContentPane().validate();
      envi.getContentPane().repaint();
      
      // Calls agents update function
       for(int i = 0; i<agents.size();i++)
       {
           System.out.println("Agent Size: "+agents.size());
           if(agents.get(i).isAlive) { //get Peripheral sensors, evaluate fitness
               agents.get(i).update();
               agents.get(i).p0 = agents.get(i).getProx(0, obsList);
               //System.out.println("p0: "+agents.get(i).getProx(0,obsList));
               agents.get(i).p1 = agents.get(i).getProx(1, obsList);

               if((agents.get(i).timeAlive - agents.get(i).timeSpawned)/1000 > 10)
               {
                   agents.get(i).isAlive = false;
               }

               m.update();

               double dx = 90;
               double dy = 90;

               System.out.println("dx = "+dx+"   "+"dy = "+dy);
               System.out.println("Agent xPos: "+(agents.get(i).x1+agents.get(i).x3)/2+"        "+"yPos: "+(agents.get(i).y1+agents.get(i).y3)/2);

               int xCord = (int)((((agents.get(i).x1+agents.get(i).x3)+90)/2)/dx);
               int yCord = (int)((((agents.get(i).y1+agents.get(i).y3)+90)/2)/dy);
               int bushFireIndex = xCord+m.randomBoardSize*yCord;
               if(agents.get(i).currentBushIndex != bushFireIndex)
               {
                   agents.get(i).timeStayedinPosStart = System.currentTimeMillis();
                   agents.get(i).timeStayedinPosEnd = System.currentTimeMillis();
                   agents.get(i).currentBushIndex = bushFireIndex;
               }else{
                   agents.get(i).timeStayedinPosEnd = System.currentTimeMillis();
               }

               if((agents.get(i).timeStayedinPosEnd - agents.get(i).timeStayedinPosStart)/1000 > timeRemainSpaceThresh)
               {
                   System.out.println("Stayed in position too long");
                   agents.get(i).isAlive = false;
               }

               double max = -Double.MAX_VALUE;
               double bushMAX = 0;

               for(double bush : m.bushfire)
               {
                    if(bush > max)
                    {
                        bushMAX = bush;
                        max = bush;
                    }
               }
                try {
                    int bushVal = m.bushfire[bushFireIndex];

                    //get bushfire

                    agents.get(i).computeFitness(bushVal, bushMAX);
                    System.out.println("PRE COLLISION CHECK");

                    if (agents.get(i).isCollided(obsPos)) {
                        System.out.println("Collided");
                        agents.get(i).isAlive = false;
                        print("Working");
                    }

                    counter++;
                } catch(Exception e){
                   System.out.println("Error With Agents");
                   agents.removeAll(agents);
                }
               //System.out.println(counter);

           }else{
               stop(agents.get(i));
               agents.remove(i);
               System.out.println("Agent Removed");
               i--;
           }

           try {
               Thread.sleep(1);
           } catch (InterruptedException e1) {
               e1.printStackTrace();
           }
           //print(p0 + "," + p1);

       }

       if(agents.size() == 0)
       {

           int i;
           int j;

           envi.getContentPane().removeAll();

           Organism bestFitOrganism = envi.nTool.EvaluateGeneration();
           envi.networkJsonEncoder.exportNetwork(bestFitOrganism.getGenome());
           System.out.println("Evaluated New Generation\nGeneration: "+envi.nTool.getGeneration());
           envi.agentList = new Agent[envi.population];

           counter = 0;

           for(i=0;i<Population.getPopulationSize();i++)
           {
               for(j=0;j<Population.getPopulationElement(i).getSpeciesSize();j++)
               {
                   envi.agentList[counter] = new Agent(135,90,Population.getPopulationElement(i).getSpeciesElement(j));
                   envi.agentList[counter].timeSpawned = System.currentTimeMillis();
                   envi.agentList[counter].timeStayedinPosStart = System.currentTimeMillis();
                   counter++;
               }
           }


           m = new Maze(5,5, envi.agentList[0]);
//     Object o = o1;


           envi.add(envi.agentList[0]);
           envi.agentList[0].add(m);
           for(i = 1; i < envi.agentList.length; i ++) {
               envi.agentList[0].add(envi.agentList[i]);
           }


           obsPos = new double[m.obsList.size()][4];

           obsList = new Object[m.obsList.size()];
           for(i = 0 ; i < m.obsList.size(); i++) {
               obsPos[i] = m.obsList.get(i).getPos();
           }
           for(i = 0 ; i < m.obsList.size(); i++) {
               obsList[i] = m.obsList.get(i);
           }
           m.update();

           counter = 0;




           envi.nTool.incrementGeneration();
           for(Agent agent:envi.agentList)
           {
               agents.add(agent);
               System.out.println("New Agent isAlive: "+agent.isAlive);
               System.out.println("New Agent xPos: "+agent.x);
               System.out.println("New Agent yPos: "+agent.y);
               System.out.println("New Agent timeStayedinPosStart: "+agent.timeStayedinPosStart);
               System.out.println("New Agent timeStayedinPosEnd: "+agent.timeStayedinPosEnd);
           }

       }

   }
 }




 public static void right(Agent a) {



   a.rightSpeed = -60;
      a.leftSpeed = 60;
 }
 public static void left(Agent a) {
   a.rightSpeed = 60;
      a.leftSpeed = -60;
 }
 public static void forward(Agent a) {
   a.rightSpeed = 60;
      a.leftSpeed = 60;
 }
 public static void stop(Agent a) {
   a.rightSpeed = 0;
      a.leftSpeed = 0;
 }
 public static void print(String input) {
   System.out.println(input);
 }
} 