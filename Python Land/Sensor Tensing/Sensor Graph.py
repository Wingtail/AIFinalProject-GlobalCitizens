import matplotlib.pyplot as plt
import threading
import time
from HamsterAPI.comm_ble import *
import sys
import json
import Tkinter as tk


def graphit(robotList):
    print("Started")
    proxL = []
    proxR = []
    count = 0
    superCount = 0
    while True:
        for robot in robotList:
            if robot:
                count += 1
                robot.set_wheel(0,10)
                robot.set_wheel(1,11)
                '''
                for i in range (0, 11):
                    robot.set_wheel(0,10)
                    robot.set_wheel(1,11)

                    prox = (robot.get_proximity(0),robot.get_proximity(1))
                    print("Prox L: " + str(prox[0]) + "\tProx R: " + str(prox[1]))
                    time.sleep(0.1)
                    proxL.append(prox[0])
                    proxR.append(prox[1])

                    if count %20 == 0:
                        count = 0
                        superCount += 1
                        fig = plt.figure()
                        plt.title("Sensor Data")
                        plt.ylabel("Sensor Reading")
                        plt.plot(proxL, label='Left Sensor')
                        plt.plot(proxR, label='Right Sensor')
                        plt.legend(loc=2)
                        fig.savefig("Fig: " + str(superCount))

                        
                        popJson = json.dumps((proxL, proxR))
                        file = "data.json"
                        sensorData = open(file, "w")
                        sensorData.truncate(0)
                        sensorData.write(popJson)

                        if superCount > 5:
                            break

                    #time.sleep(10)
                    robot.set_wheel(0,-10)
                    robot.set_wheel(1,-10)
                    time.sleep(10)

                '''
                if superCount < 10:
                    prox = (robot.get_proximity(0),robot.get_proximity(1))
                    print("Prox L: " + str(prox[0]) + "\tProx R: " + str(prox[1]))
                    time.sleep(0.1)
                    proxL.append(prox[0])
                    proxR.append(prox[1])

                    if count %20 == 0:
                        count = 0
                        superCount += 1
                        fig = plt.figure()
                        plt.title("Sensor Data")
                        plt.ylabel("Sensor Reading")
                        plt.plot(proxL, label='Left Sensor')
                        plt.plot(proxR, label='Right Sensor')
                        plt.legend(loc=2)
                        fig.savefig("Fig: " + str(superCount))

                        
                        popJson = json.dumps((proxL, proxR))
                        file = "data.json"
                        sensorData = open(file, "w")
                        sensorData.truncate(0)
                        sensorData.write(popJson)
                    # plt.show()
'''    

def approx_function()    

     
    import json

    from pprint import pprint

    with open('data.json') as data_file:    
        data = json.load(data_file)
    pprint(data)     

    #16cm distance -> begins sensing 
    #0.2cm distance -> stops sensing

    #(proxL[i], proxR[i])     
    #sensorData = ((1/350)x^2) - 7    
    #Quadratic least squares regression: y=((6.629324757*10^-4)*(x^2)+(5.460188952*10^-1)x-(5.589507474
    #Where y = right sensor data and x = proximity samples. x = 1 means first sample that sensed the obstacle
'''

def main():
    max_robot_num = 1   # max number of robots to control
    comm = RobotComm(max_robot_num)
    comm.start()
    print 'Bluetooth starts'
    robotList = comm.robotList
    plt.show(block=True)
    print("Creating Thread")
    thread = threading.Thread(name="Graph Thread", target=graphit, args=[robotList])
    thread.daemon = True
    thread.start()
  
    root = tk.Tk()
    root.mainloop()


    comm.stop()
    comm.join()

if __name__== "__main__":
  sys.exit(main())

def __main__():
    main()