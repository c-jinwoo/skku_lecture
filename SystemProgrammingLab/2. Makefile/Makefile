# SystemProgrammingLab
# Week1 - Makefile
# 2020315798 ChoiJinWoo

TARGET=week1
CC=gcc
AS=-W
OBJS=main.o plus.o minus.o

$(TARGET): $(OBJS)
	$(CC) $(AS) -o $@ $^
main.o: main.c
	$(CC) $(AS) -c $^
plus.o: plus.c
	$(CC) $(AS) -c $^
minus.o: minus.c
	$(CC) $(AS) -c $^
clean:
	rm $(TARGET) $(OBJS)
