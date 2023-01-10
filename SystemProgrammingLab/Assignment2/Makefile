# System Programming Lab
# Project Assignment 2
# Choi Jin Woo
# 2020315798

TARGET=pa2.out
CC=gcc
AS=-W
OBJS=server.c

$(TARGET): $(OBJS)
	$(CC) $(AS) -o $@ $^ -lpthread
clean:
	rm $(TARGET) $(OBJS)
