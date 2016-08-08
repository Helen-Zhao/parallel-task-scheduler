1. Create ProcessAllocator interface
 - Requires AllocateProcessor method
2. AllocateProcessor(nodeToAllocate, listOfAllocatedNodes, unavailableProcessors)
 1. Look at all dependencies of nodeToAllocate
 2. Find earliest possible startTime in available processors  
    - Ensure does not overlap with runtime of a previously selected node
    - Ensure it occurs after communications time (if on new processor) or disregard if on same as **last** dependency, after comms time for dependencies on other processors
 3. Set startTime and processor in nodeToAllocate
 4. return some status -- ie. allocatedProcessor or noAvailableProcessors (true/false)
3. FindStartTime(nodeToAllocate, listOfAllocatedNodes, processor)
 1. Find earliest startTime for nodeToAllocate on specified processor 
