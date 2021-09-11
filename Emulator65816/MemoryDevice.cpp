#include <malloc.h>
#include <string.h>
#include "MemoryDevice.h"

MemoryDevice::MemoryDevice(uint32_t size)
{
	pvMemory = (uint8_t*)malloc(size);
	if (pvMemory)
	{
		memset(pvMemory, 0xea, size);
		this->size = size;
	}
	else
	{
		this->size = 0;
	}
}


MemoryDevice::~MemoryDevice()
{
	free(pvMemory);
	size = 0;
	pvMemory = NULL;
}

void MemoryDevice::storeByte(const Address& address, uint8_t data)
{
	uint8_t bank = address.getBank();
	uint16_t offset = address.getOffset();

	uint32_t i = bank * 64 KB + offset;
	pvMemory[i] = data;
}


uint8_t MemoryDevice::readByte(const Address& address)
{
	uint8_t bank = address.getBank();
	uint16_t offset = address.getOffset();

	uint32_t i = bank * 64 KB + offset;
	return pvMemory[i];
}


bool MemoryDevice::decodeAddress(const Address& address, Address& decodeAddress)
{
	uint8_t bank = address.getBank();
	uint16_t offset = address.getOffset();
	uint32_t i = bank * 64 KB + offset;
	if (i < size)
	{
		decodeAddress = address;
		return true;
	}
	else
	{
		return false;
	}
}

uint8_t charToHex(char c)
{
	if (c >= '0' && c <= '9')
	{
		return c - '0';
	}
	if (c >= 'A' && c <= 'F')
	{
		return c - 'A' + 0x0A;
	}
	return 0xff;
}

void MemoryDevice::set(uint32_t address, const char* szBytes)
{
	size_t length = strlen(szBytes);
	if (length % 2 == 1)
	{
		return;
	}

	for (uint32_t i = 0; i < length; i += 2, address++)
	{
		char hi = szBytes[i];
		char lo = szBytes[i+1];

		uint8_t value = charToHex(hi) * 0x10 + charToHex(lo);
		pvMemory[address] = value;
	}
}

