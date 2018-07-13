import sys
import time
import threading
import Tkinter as tk
from HamsterAPI.comm_ble import RobotComm   # no dongle
#from HamsterAPI.comm_usb import RobotComm  # yes dongle

################################
# Hamster control
################################
class RobotBehaviorThread(threading.Thread):
    def __init__(self, robotList):
        super(RobotBehaviorThread, self).__init__()
        self.go = False
        self.done = False
        self.to_proximity = False

        self.robotList = robotList
        return

    def run(self):
        robot=None
        while not self.done:
            for robot in self.robotList:
                if robot and self.go:

                    if self.to_proximity:    
                        def proximity(self):

                          
                            total_prox = 0

                            for i in range (0,100000):

                                lprox = robot.get_proximity(0)
                                rprox = robot.get_proximity(1)
                                
                                prox = (lprox+rprox)/2

                                if i==0:
                                    minProx = prox
                                    maxProx = prox

                                elif prox < minProx:
                                    minProx = prox  

                                elif prox > maxProx:
                                    maxProx = prox      


                                total_prox = total_prox + prox


                            avg_prox = total_prox/100000
                            print "avg prox is: ", avg_prox
                            print "min prox is: ", minProx
                            print "max prox is: ", maxProx
                            print " "


                        proximity(self)

        if robot:
            robot.reset()
            time.sleep(0.1)
        return

class GUI(object):
    def __init__(self, root, robot_control):
        self.root = root
        self.robot_control = robot_control
        root.geometry('400x30')
        root.title('Hamster Control')

        b1 = tk.Button(root, text='Go')
        b1.pack(side='left')
        b1.bind('<Button-1>', self.startProg)

        b2 = tk.Button(root, text='Exit')
        b2.pack(side='left')
        b2.bind('<Button-1>', self.stopProg)

        

        b7 = tk.Button(root, text='Prox')
        b7.pack(side='left')
        b7.bind('<Button-1>', self.to_proximity)
        
        return
    
    def startProg(self, event=None):
        self.robot_control.go = True
        return

    def stopProg(self, event=None):
        self.robot_control.done = True      
        self.root.quit()    # close window
        return

         
        

    def to_proximity(self, event=None):
        self.robot_control.to_proximity = True      
        return              


def main():
    # instantiate COMM object
    gMaxRobotNum = 1; # max number of robots to control
    comm = RobotComm(gMaxRobotNum)
    comm.start()
    print 'Bluetooth starts'  
    robotList = comm.robotList

    behaviors = RobotBehaviorThread(robotList)
    behaviors.start()

    frame = tk.Tk()
    GUI(frame, behaviors)
    frame.mainloop()

    comm.stop()
    comm.join()
    print("terminated!")

if __name__ == "__main__":
    sys.exit(main())