from brute_force import baseline, pareto_optimal

import matplotlib.pyplot as plt
# from mpl_toolkits.mplot3d import Axes3D

def compare_result(file_dir_list, pareto_output=None):
    skylines = []

    # Read thje data from each file and store them in skylines
    for file in file_dir_list:
        with open(file, 'r') as f:
            lines = f.readlines()
            lines = set(tuple(line.strip().split()) for line in lines)
            skylines.append(lines)

    # If pareto_output is provided, add it to skylines
    # Check if all skyline results are identical to confirm Pareto Optimality.    
    if pareto_output:
        skylines.append(set(tuple(line) for line in pareto_output))
        if all(x == skylines[0] for x in skylines):
            print('Pareto Correct!')
        else:
            print('Pareto Incorrect!')

    # If more than one files are provided, check if all outputs are identical.
    if len(file_dir_list) > 1:
        if all(x == skylines[0] for x in skylines):
            print('Same output:)')
        else:
            print('Different output:(')

def plot(pareto_skyline, pareto_not_skyline):

    # Take Pareto skyline and non-skyline points as input and visualize them on a 3D plot. 

    _, _, x, y, z = zip(*pareto_skyline)
    x = [int(i) for i in x]
    y = [int(i) for i in y]
    z = [int(i) for i in z]

    _, _, nx, ny, nz = zip(*pareto_not_skyline)
    nx = [int(i) for i in nx]
    ny = [int(i) for i in ny]
    nz = [int(i) for i in nz]

    fig = plt.figure()
    ax = fig.add_subplot(111, projection='3d')
    ax.scatter(y, x, z, color='black', marker='x', alpha=0.7)
    ax.scatter(ny, nx, nz, color='orange', marker='^', alpha=0.1)
    ax.set_xlabel('service')
    ax.set_ylabel('quality')
    ax.set_zlabel('price')

    plt.savefig('problem_1_1.png')

def make_output(title, skyline):
    with open(f'./output/{title}.txt', 'w') as f:
        for line in skyline:
            f.write(" ".join(line))
            f.write("\n")
    

if __name__ == '__main__':
    """
    You can follow each step's progress using this script.

    For problem 1, you can check the implementation of pareto_optimal function with the compare_result function in this file.

    To get the output files, follow the instructions below:

        For problem 1-(1), you can just use the script to get the output file.

        For problem 1-(2), and 2 use the following command to get the output file from HDFS to Local:
            From HDFS to docker container
                $ hdfs dfs -copyToLocal /hw/output/part-00000 .
            From docker container to local (You must run this command from your local directory): 
                $ docker cp <containerId>:<directory which has part_00000> .
            Change file names in your local directory
                $ mv part-00000 <your file name>.txt
     """

    # Load data
    input_file = './data/input_check.txt' # './data/restaurant.txt'

    # This is the step to check if pareto_optimal function is implemented correctly.
    pareto_skyline, pareto_not_skyline = pareto_optimal(input_file)
    compare_result(['./output/output_check.txt'], pareto_output=pareto_skyline)

    # Step to answer question 1
    input_file = './data/restaurant.txt'
    pareto_skyline, pareto_not_skyline = pareto_optimal(input_file)
    plot(pareto_skyline, pareto_not_skyline)
    make_output('p1_1', pareto_skyline)
    
    # If you finish question 2 and get the output file, you can use the following script to check if your output is correct.
    file_dir_list = ['./output/p1_1.txt', './output/p1_2.txt', './output/p2.txt']
    compare_result(file_dir_list)