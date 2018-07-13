import json
import Queue as Q
import math

class Network():
	weightDict = None
	reverseWeightDict = None
	nodes = None

	def __init__(self):
		self.weightDict = {}
		self.reverseWeightDict = {}
		self.nodes = set()

	def getNetworkFromFile(self, file):
		
		file = open(file,"r")
		netJson = file.read()
		netList = json.loads(netJson)
		
		#netList in form [fromNode, toNode, wieght]
		# netList = [[1,3,1],[2,3,1],[2,4,0.5],[3,3,10],[3,4,1]]
		weightDict = {}
		nodes = set()
		for netItem in netList:
			nodes.add(netItem[0])
			nodes.add(netItem[1])
			if netItem[0] not in weightDict.keys():
				weightDict[netItem[0]] = [(netItem[1],netItem[2])]
			else:
				weightDict[netItem[0]].append((netItem[1],netItem[2]))
		print weightDict
		print(nodes)

		self.weightDict = weightDict
		self.nodes = nodes
		for node in nodes:
			self.reverseWeightDict[node] = set()
		for node1 in weightDict.keys():
			for connect in weightDict[node1]:
				self.reverseWeightDict[connect[0]].add(node1)


	def propagate(self, inputs):
		NUM_INPUTS = 3
		nodeSum = {}
		activations = {}
		nodeDones = set()
		isWaiting = set()
		for node in self.nodes:
			nodeSum[node] = 0
		#nodes start at 1, Thanks Brian
		for i in range(len(inputs)):
			nodeSum[i+1] = inputs[i]
			# print("Node " + str(i+1)+" is inputed: "+str(inputs[i]))


		q = Q.Queue()
		for i in range(len(inputs)):
			q.put(i+1)

		print(self.weightDict)


		while not q.empty():
			# print(q.qsize())
			node = q.get()
			# print("Got Node %s from Queue" % str(node))
			if node in nodeDones:
				# print("NODE IS IN NODES DONE")
				# print("Node is done")
				continue
			if node in isWaiting:
				# print("NODE WAS IN isWaiting")
				# print("Node was in isWaiting")
				isWaiting.remove(node)
				# print("NODE WAS REMOVED FROM WAITING")
			
			# node = q.get()
			# print()
			waiting = False
			for parent in self.reverseWeightDict[node]:
				# print("Looking at parnt: " + str(parent))
				if parent not in nodeDones and parent not in isWaiting:
					waiting = True
			if waiting:
				# print("IS WAITING")
				isWaiting.add(node)
				q.put(node)
				# nodeDones.remove(node)
				continue
			else:
				nodeDones.add(node)
			# print("Activating")
			a = sig(nodeSum[node])
			activations[node] = a
			nodeSum[node] = 0
			if node not in self.weightDict.keys():
				# print("Node not in weightDict")
				continue
			print("EVALUATING NODE: " +str(node))
			print("NODE ACTIVATION: " + str(a))
			for edge in self.weightDict[node]:
				print("Edge: "+str(edge)+", current sum: " + str(nodeSum[edge[0]]))

				nodeSum[edge[0]] += a * edge[1]
				if edge[0] in nodeDones:
					activations[edge[0]] = sig(nodeSum[edge[0]])	
				else:
					# print("PUT EDGE")
					q.put(edge[0])

				print("Edge: "+str(edge)+", after sum: " + str(nodeSum[edge[0]]))

			


		print("OUT OF WHILE LOOP")
		# print(activations)
		return activations[max(self.nodes)]*2-1


def sig(x):
	# print(x)
	y = (1/(1+math.exp(-x)))
	# print(y)
	# y = x
	return y
	# return x


if __name__ == "__main__":
	net = Network()
	net.getNetworkFromFile("Network.json")
	result = net.propagate([4,4,3])
	print(result)

