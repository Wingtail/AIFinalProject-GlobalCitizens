import sys
sys.path.append('../')
import time  # sleep
import threading
import logging
import Tkinter as tk
import Queue
import random
from HamsterAPI.comm_ble import RobotComm
import propagation


def mainThread(robotList):
	#Check if at goal
	FLOOR_THRESHHOLD = 35
	while False != True:
		for robot in robotList:
			robot.set_musical_note(0)
			if robot.get_floor(0) > FLOOR_THRESHHOLD or robot.get_floor(1) > FLOOR_THRESHHOLD:
				robot.set_musical_note(40)
				robot.set_wheel(0,0)
				robot.set_wheel(1,0)
				time.sleep(1)
				


			proxL = robot.get_proximity(0)
			proxR = robot.get_proximity(1)

			net = propagation.Network()
			net.getNetworkFromFile("CurrentNet.json")
			outputs = net.propagate([proxL,proxR])
			print("Outputs: " + str(outputs))

			robot.set_wheel(0, int(outputs[0]*100))
			robot.set_led(0, int(outputs[0]*8))
			robot.set_wheel(1, int(outputs[1]*100))
			robot.set_led(1, int(outputs[1]*8))

			if (int(outputs[0]*100) == 0 and int(outputs[1]*100) == 0):
				robot.set_wheel(0, 17)
				robot.set_wheel(1, 17)


			time.sleep(0.01)




def main():
    max_robot_num = 1   # max number of robots to control
    comm = RobotComm(max_robot_num)
    comm.start()
    print 'Bluetooth starts'
    robotList = comm.robotList

    root = tk.Tk()
    # t_handle = BehaviorThreads(robotList)
    # gui = GUI(root, t_handle)
  
    mThread = threading.Thread(name="main thread", target=mainThread, args=[robotList])
    mThread.daemon = True
    mThread.start()


    root.mainloop()


    comm.stop()
    comm.join()

if __name__== "__main__":
  sys.exit(main())