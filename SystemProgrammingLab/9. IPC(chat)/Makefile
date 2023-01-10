# SystemProgrammingLab
# Week9 - IPC(Chatting program)
# ChoiJinWoo
# 2020315798

TARGET=ex9
CC=gcc
AS=-W
OBJS=main.o

$(TARGET): $(OBJS)
	$(CC) $(AS) -o $@ $^
main.o: main.c
	$(CC) $(AS) -c $^
clean:
	rm $(TARGET) $(OBJS)
