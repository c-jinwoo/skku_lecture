# SystemProgrammingLab
# Week6 - Daemon
# ChoiJinWoo
# 2020315798

TARGET=cron
CC=gcc
AS=-W
OBJS=cron.o

$(TARGET): $(OBJS)
	$(CC) $(AS) -o $@ $^
cron.o: cron.c
	$(CC) $(AS) -c $^
clean:
	rm $(TARGET) $(OBJS)
