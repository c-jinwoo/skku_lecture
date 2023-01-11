TARGET=main
CC=gcc
AS=-w
OBJS=main.o

$(TARGET): $(OBJS)
	$(CC) $(AS) -o $@ $^
main.o: main.c
	$(CC) $(AS) -c $^
clean:
	rm $(TARGET) $(OBJS)