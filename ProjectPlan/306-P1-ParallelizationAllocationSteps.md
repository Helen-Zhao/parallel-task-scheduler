# Parallelization Allocation Steps

[Breadth-First Search Tutorial](https://www.youtube.com/watch?v=QRq6p9s8NVg)

---

## Creating Partial Schedules using Breadth-First Search

*We create partial schedules to find an adequete number of subpath starting points for our Depth-First search schedulers.*

### Storing Iteration Progress of Search

We want to search the entirety of one level of our search tree (the stack of queues of nodes, stretching across to all potential queues on that level) 
 for sub tree starting points for our DFS schedulers.  The ***primary*** nodes in each queue (ie. the starting point of the sub tree) can be stored in a 
 ***BFS Queue*** as a **tuple** of the *node stack* its *nodelist* and *processorAllocator*.  
 Each tuple should contain ***seperate*** node stacks and lists, as in stacks/lists in different tuples are clones of each other.  
 This way you can easily iterate the primary node (first node of the lowest level queue) of each node stack until *allocateProcessor* returns false, at which 
 point you can *pop* the tuple of the queue and begin with the next primary node.  

 ---

### Initializing the BFS Queue and Iterating Through

The initial BFS queue will consist of the **root nodes** and **empty node stacks**.  Until you have a number of subpaths greater than your heuristic requirement, 
 you will *peek* the first node of the queue and pass it to *allocateProcessor*.  By passing in the nodeList to the nodeFinders *findSatisfiedNodes* function 
 you will acquire the next set of nodes in the next level of the search tree.  You will then add a ***clone*** of the stack, list and allocator onto the BFS Queue.

You will *pop/get* (remove) the head of the queue when the allocator returns false, as there are no more processors to allocate, and start on the next node in the Queue.

You should keep track of the total number of nodes on a level such that you can evaluate whether nodes can be allocated from that level as initial partial schedules 
 to a DFS scheduler.  

 ---

#### Estimating the Required Level

The number of subpaths that can be found at each level are as follows, assuming the only paths to be pruned continues to be symmetric paths:  
 - **L<sub>0</sub>**  *= numNodes x 1*
 - **L<sub>1</sub>**  *= numNodes x Minimum(2, numProcessors)*
 - **L<sub>2</sub>**  *= numNodes x Minimum(3, numProcessors)*
 - so on, the initial value increases by one per level  

Upon evaluating all nodes from a level, you should be able to estimate the total number of subpaths present in the subsequent level.  If this estimate is greater than  
 the required number of subpaths, you can begin creating schedulers and allocating them each subpath on the BFS Queue.

 ---

#### Allocating a Node Stack

Upon reaching a level with an appropriate number of subpaths, you can start creating and allocating subpaths to DFS schedulers.  To do this you should *peek* the front of the BFS Queue. 
You will then allocate a processor for the front of the nodeStack, and pass a ***clone*** of it to the first created scheduler.  

You will *pop/get* the lowest level of the nodeStack when the allocator returns false, and continue until the lowest level of the nodeStack is empty, at which point you will *pop/get* 
the nodeStack from the **BFS Queue** and start again with the next nodeStack in the BFS Queue.

---

## Example

***3 Threads, 2 processors, 4 subpaths required (estimate)***

0, 1, 2, 3, ...  
0 -> 1,  
0 -> 2,  
0 -> 3

There is 1 node on L0 and 3 nodes one L1.  There are more nodes that depend on 1,2,3 but we don't care about them in this example.  

1. We find node 0 as the root node and allocate it to the BFS Queue.
2. We use *findSatisfiedNodes* and add the satisified nodes to the BFS Queue.
  - We add the satisfied nodes by adding a clone of the created nodestack, nodelist, and allocator to the BFS Queue
  - As we retained the number of nodes of L0 (1), we know we have finished L0.
  - We add the found nodes to the number of nodes known to be on L1.  This number would continue to grow if we had more root nodes.
3. The number of nodes on L1 x 2 = 6 (3nodes x 2processors).  This is greater than our estimate of 4 subpaths so we begin creating schedulers and allocating the subpaths.
4. We peek the nodestack at the head of the queue and use *allocateProcessor* on the head of the lowest level queue.  We then pass a clone of the nodeStack, list, and allocator to the new scheduler.
  - We will pass node 1, processor 1 to the first scheduler. We will pass node 2, processor 2 to the first scheduler after it completes the subpath.
  - We will pass node 1, processor 2 to the second scheduler. We will pass node 3, processor 1 to the second scheduler after it completes the subpath.
5. Continue step 4 until allocator returns false.
6. Upon false, pop the head of the queue and repeat step 4-5.
  - We will pass node 2, processor 1 to the third scheduler.  We will pass node 3, processor 2 to the third scheduler after it completes the subpath.
7. Continue until the lowest level of the nodeStack is empty, when all schedulers return the optimal path should have been found.

NOTE: For this example I have assumed the schedulers will complete their subpaths sequentially (scheduler 1, then 2, then 3).  This will likely not happen in practice, but should be irrelevant.

---

## What To Do

**You need to have some sort of while loop that continues to perform Breadth-First search until an adequete number of subpaths are found (while numSubPaths < req).**  

**You will need to have other while loops nested, one for a level (while numNodesThisLevel > 0), another for a node (while allocateProcessor(node)).**  

**After the BFS loop, you will need to be able to compare optimal schedules and give new schedulers the next node on the lowest level of the current nodeStack until all nodes**
**on the lowest level of all nodeStacks have been allocated and all schedulers have completed their provided subpath/s.**

---

## Pseudo-Code 

Not now.