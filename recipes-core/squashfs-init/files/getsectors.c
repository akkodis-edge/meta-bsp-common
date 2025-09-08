#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <linux/fs.h>

static void print_usage(void)
{
	printf("getsectors, get blockdev sector count or size\n");
	printf("getsectors [OPTIONS] DEVICE");
	printf("Mandatory:\n");
	printf("  --size,      Size of sectors\n");
	printf("or\n");
	printf("  --count,     Number of sectors\n");
	printf("Returns:\n");
	printf("  On success returns 0\n");
}

enum operation {
	OP_NONE,
	OP_COUNT,
	OP_SIZE,
};

int main(int argc, char* argv[])
{
	char *device = NULL;
	int op = OP_NONE;

	for (int i = 1; i < argc; ++i) {
		if (strcmp("-h", argv[i]) == 0 || strcmp("--help", argv[i]) == 0) {
			print_usage();
			return EXIT_FAILURE;
		}
		else if (strcmp("--count", argv[i]) == 0) {
			op = OP_COUNT;
		}
		else if (strcmp("--size", argv[i]) == 0) {
			op = OP_SIZE;
		}
		else {
			device = argv[i];
		}
	}

	if (device == NULL) {
		fprintf(stderr, "Invalid or missing DEVICE\n");
		return EXIT_FAILURE;
	}
	if (op != OP_COUNT && op != OP_SIZE) {
		fprintf(stderr, "Missing mandatory argument --count or --size\n");
		return EXIT_FAILURE;
	}

	const int fd = open(device, O_RDONLY);
	if (fd < 0) {
		fprintf(stderr, "Failed opening DEVICE [%d]: %s\n", errno, strerror(errno));
		return EXIT_FAILURE;
	}

	int r = 0;
	int sector_size = 0;
	if (ioctl(fd, BLKSSZGET, &sector_size) != 0) {
		r = errno;
		fprintf(stderr, "Failed ioctl BLKSSZGET [%d]: %s\n", r, strerror(r));
		goto exit;
	}
	if (op == OP_SIZE) {
		printf("%d\n", sector_size);
		goto exit;
	}

	size_t size_bytes = 0;
	if (ioctl(fd, BLKGETSIZE64, &size_bytes) != 0) {
		r = errno;
		fprintf(stderr, "Failed ioctl BLKGETSIZE64 [%d]: %s\n", r, strerror(r));
		goto exit;
	}
	printf("%zu\n", size_bytes / sector_size);

	r = 0;
exit:
	if (close(fd) != 0) {
		fprintf(stderr, "Failed closing DEVICE [%d]: %s\n", errno, strerror(errno));
		if (r == 0)
			r = errno;
	}
	return r;
}
