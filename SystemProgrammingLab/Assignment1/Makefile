# SystemProgrammingLab
# Assignment 1
# ChoiJinWoo
# 2020315798

TARGET=minishell
CC=gcc
AS=-W
OBJS=main.o

$(TARGET): $(OBJS)
	$(CC) $(AS) -o $@ $^
main.o: main.c
	$(CC) $(AS) -c $^
clean:
	rm $(TARGET) $(OBJS)
