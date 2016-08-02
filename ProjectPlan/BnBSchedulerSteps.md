1. Create Scheduler interface
2. Requires CreateSchedule(nodeList) method returning nodeList with startTimes and processors allocated
3. Scheduler:
 1. Calculate and store heuristic bound (initially sum of all node weights, then total weight of shortest path)
 2. Pass entire node list to AvailableNodeFinder, returns nodes able to be allocated, select first node (simple algorithm)
 3. First node startTime = 0, processor = 1, first node in current schedule, remove from available nodes
   - Maintain nodeLists: currentSchedule, optimalSchedule, unallocatedNodes, availableNodes
 5. send chosen node to AvailableNodeFinder, add new nodes to available
 6. select next node, select processor, repeat 5-6
 7. upon no new processor available, pop node and select different node
     - Need some kind of stack maintaining traversed paths
 8. upon no new nodes available, pop node and select different node
 9. Store shortest schedule and pathTime as required