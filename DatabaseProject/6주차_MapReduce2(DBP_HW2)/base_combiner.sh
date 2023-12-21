hdfs dfs -rm -r /hw/output*

t1=$(date +%s%3N);
hadoop jar /opt/hadoop-3.2.1/share/hadoop/tools/lib/hadoop-streaming-3.2.1.jar \
	-D mapreduce.job.maps=4 \
	-D mapreduce.job.reduces=1 \
	-files mapper.py,reducer.py,combiner.py \
	-input /hw/input \
	-output /hw/output \
	-mapper mapper.py \
	-combiner combiner.py \
	-reducer reducer.py
t2=$(date +%s%3N);
echo "Elapsed time: $((t2-t1)) ms"

