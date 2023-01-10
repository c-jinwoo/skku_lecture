# SystemProgrammingLab
# Week7 - IPC
# ChoiJinWoo
# 2020315798

TARGET=main
CC=gcc
AS=-W
OBJS=main.o

$(TARGET): $(OBJS)
	$(CC) $(AS) -o $@ $^
cron.o: cron.c
	$(CC) $(AS) -c $^
clean:
	rm $(TARGET) $(OBJS)
