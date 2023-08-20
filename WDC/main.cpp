char getChar(char c, char* sz, int iLength)
{
	int i;
	for (i = 0; i < iLength; i++)
	{
		if (sz[i] == '\0')
		{
			return 0xff;
		}

		if (sz[i] == c)
		{
			return sz[i];
		}
	}
	return 0xff;
}


char gasz[6];

void main(void)
{
	int i;
	gasz[0] = 'H';
	gasz[1] = 'e';
	gasz[2] = 'l';
	gasz[3] = 'l';
	gasz[4] = 'o';
	gasz[5] = '\0';

	for (i = 0; i < 4; i++)
	{
		getChar('A' + i, gasz, 5);
	}
}

