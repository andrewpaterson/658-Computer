#ifndef __MEMORY_DEVICE_H__
#define __MEMORY_DEVICE_H__
#include "SystemBusDevice.h"

#define KB * (1024)

class MemoryDevice : public SystemBusDevice
{
private:
    uint32_t    size;
    uint8_t*    pvMemory;

public:
    MemoryDevice(uint32_t size);
    ~MemoryDevice()  override;

    void storeByte(const Address& address, uint8_t data) override;

    uint8_t readByte(const Address& address) override;

    bool decodeAddress(const Address& address, Address& decodeAddress) override;

    void set(uint32_t address, const char* szBytes);
};


#endif // !__MEMORY_DEVICE_H__

