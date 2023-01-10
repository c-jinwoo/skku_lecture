# SystemProgrammingLab
# Week4 - FileIO
# ChoiJinWoo
# 2020315798

TARGET=week5
CC=gcc
AS=-W
OBJS=main.o

$(TARGET): $(OBJS)
	$(CC) $(AS) -o $@ $^
main.o: main.c
	$(CC) $(AS) -c $^
clean:
	rm $(TARGET) $(OBJS)
