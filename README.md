# softeng-306-p1: Team 4, NP-Easy

## Team Members
| Name             | UPI          | AUID   | GitHub       |
| ---------------- |:-------------:| -----:| ------------:|
| Benjamin Collins | bcol602 | 9168328 | @MizdaCollinz |
| Helen Zhao | hzha587 | 6913580 | @Helen-Zhao |
| Jay Gradon | jgra718 | 6320267 | @jaygradon |
| William Lin | wlin943 | 6820093 | @OphielOM |
| Jacky Mai | jmai871 | 8572038 | @JackyMai |
| Benjamin Collins | bcol602 | 9168328 | @MizdaCollinz |
| Henry Wu | hwu668 | 9076300 | @HellScytheX |

## Overview
A Directed Acyclic Graph in dot file form is used to model tasks and dependencies between tasks. This program schedules
tasks with a start time, and assigns it to a processor so as to return the schedule with the lowest overall run time.

Inputs: 	&#60;input-file-name&#62; 	&#60;number of processors&#62; 	&#60;optional-flags&#62;

Optional Flags:

&nbsp;&nbsp;&nbsp;&nbsp;-o OUTPUTNAME&nbsp;&nbsp;&nbsp;&nbsp;Specify output name of choice. Default is input name with -output appended.

&nbsp;&nbsp;&nbsp;&nbsp;-v&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Visualise the scheduling process

&nbsp;&nbsp;&nbsp;&nbsp;-p NUMTHREADS&nbsp;&nbsp;&nbsp;&nbsp;Parallelise the process by using multiple cores/threads

This program will output a dot file to the current working directory with fields for the start time and processor number it was allocated to. If running from a jar, this will be the directory the jar is placed in.

## Building Project from Source Code
In an IDE with Maven installed, run the Maven clean and install goals. This will generate a .jar file in the target folder.
This jar is ready to run.

## Where to Find Information
 Wiki (See side bar):
 - Meeting Minutes
 - Decisions/Issues (In Meeting Minutes)
 - Background Research
 - Documentation on the classes, their functions and their purposes
 - Dependencies (External Libraries) used
 - Development Workflow
  
ProjectPlan folder on GitHub:
 - Project Plan diagrams
 - UML diagrams
 - Algorithm plans

