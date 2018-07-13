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
		NUM_INPUTS = 2
		nodeSum = {}
		activations = {}
		nodeDones = set()
		isWaiting = set()

		#Inintializing Node Sums and setting inputs
		for node in self.nodes:
			nodeSum[node] = 0
		#nodes start at 1, Thanks Brian
		

		for i in range(len(inputs)):

			nodeSum[i+1] = inputs[i]
			activations[i+1] = sig(inputs[i])
			# propNode(i+1)
			# print("Node " + str(i+1)+" is inputed: "+str(inputs[i]))

		# print("\n###############################\nGETTING ORDER\n########################\n")
		path = self.getOrder()
		# print("\n###############################\nGot ORDER\n########################\n")
		# print("PATH")
		# print(path)

		for node in path:
			# print("propagating Node " +str(node))
			# print("Node "+str(node)+" has sum: " + str(nodeSum[node]))
			a = sig(nodeSum[node])
			activations[node] = a
			# print("Node "+str(node)+" activation: " + str(activations[node]))

			if node in self.weightDict.keys():
				for edge in self.weightDict[node]:
					# print("Pushing to node " + str(edge[0]))
					# print("Node "+str(edge[0])+" has sum: " + str(nodeSum[edge[0]]))
					nodeSum[edge[0]] += a*edge[1]
					# print("Node "+str(edge[0])+" has sum: " + str(nodeSum[edge[0]]))

		return (activations[3],activations[4])
					



		
	def getOrder(self):
		NUM_INPUTS = 3
		nodePath = []
		doneNodes = set()
		toDoNodes = set(self.nodes)

		#nodes starting at 1
		for i in range(NUM_INPUTS):
			toDoNodes.remove(i+1)
			doneNodes.add(i+1)
			nodePath = [i+1] + nodePath
		while len(toDoNodes) > 0:
			print("toDoNodes: %s" % str(toDoNodes))
			print("Path: %s" % str(nodePath))
			#Finidng node with most done dependencies
			maxPercent = -1
			nextNode = None
			for node in toDoNodes:

				psDone = 0
				for edge in self.reverseWeightDict[node]:
					if edge in doneNodes:
						psDone += 1
				if len(self.reverseWeightDict[node]) == 0:
					percent = 0
				else:
					percent = float(psDone) / float(len(self.reverseWeightDict[node]))
				print("Node %s has percent of %s" % (str(node), str(percent)))
				if percent > maxPercent:
					print("Node %s now has the maxPercent" % str(node))
					maxPercent = percent
					nextNode = node

			toDoNodes.remove(nextNode)
			doneNodes.add(nextNode)
			nodePath.append(nextNode)


		return(nodePath)

def sig(x):
	# print(x)
	y = (1/(1+math.exp(-x)))*2-1
	# print(y)
	# y = x
	return y
	# return x


if __name__ == "__main__":
	net = Network()
	net.getNetworkFromFile("Network6.json")
	result = net.propagate([4,4,3])
	print(result)
