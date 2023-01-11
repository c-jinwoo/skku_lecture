TARGET=proj3
CC=gcc
AS=-w
OBJS=OS41_2022-2_2020315798_ChoiJinWoo_P03.o

$(TARGET): $(OBJS)
	$(CC) $(AS) -o $@ $^
OS41_2022-2_2020315798_ChoiJinWoo_P03.o: OS41_2022-2_2020315798_ChoiJinWoo_P03.c
	$(CC) $(AS) -c $^
clean:
	rm $(TARGET) $(OBJS)
