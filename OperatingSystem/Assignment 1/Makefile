TARGET=main
CC=gcc
AS=-w
OBJS=main.o init.o exec.o util.o

$(TARGET): $(OBJS)
	$(CC) $(AS) -o $@ $^
util.o: util.c
	$(CC) $(AS) -c $^
exec.o: exec.c
	$(CC) $(AS) -c $^
init.o: init.c
	$(CC) $(AS) -c $^
main.o: main.c
	$(CC) $(AS) -c $^
clean:
	rm $(TARGET) $(OBJS)