#include <stdio.h>
#include "pico/stdlib.h"
#include "hardware/gpio.h"
#include "pico/binary_info.h"

const uint PIN_REGISTER_SELECT = 14;
const uint PIN_READ_WRITEB = 15;
const uint PIN_ENABLE = 5;
const uint PIN_DB0 = 13;
const uint PIN_DB1 = 12;
const uint PIN_DB2 = 11;
const uint PIN_DB3 = 10;
const uint PIN_DB4 = 9;
const uint PIN_DB5 = 8;
const uint PIN_DB6 = 7;
const uint PIN_DB7 = 6;
const uint PIN_LED = 25;

const uint eCycleTime = 1;

uint make_command_mask(bool rs, bool rw, bool e)
{
    uint rs_s = rs ? (1 << PIN_REGISTER_SELECT) : 0;
    uint rw_s = rw ? (1 << PIN_READ_WRITEB) : 0;
    uint e_s = e ? (1 << PIN_ENABLE) : 0;

    uint mask =  rs_s | rw_s | e_s;
    return mask;
}

uint make(bool rs, bool rw, bool e, uint val)
{
    uint mask = make_command_mask(rs, rw, e);

    uint v0 = val & 0x01 ? (1 << PIN_DB0) : 0;
    uint v1 = val & 0x02 ? (1 << PIN_DB1) : 0;
    uint v2 = val & 0x04 ? (1 << PIN_DB2) : 0;
    uint v3 = val & 0x08 ? (1 << PIN_DB3) : 0;
    uint v4 = val & 0x10 ? (1 << PIN_DB4) : 0;
    uint v5 = val & 0x20 ? (1 << PIN_DB5) : 0;
    uint v6 = val & 0x40 ? (1 << PIN_DB6) : 0;
    uint v7 = val & 0x80 ? (1 << PIN_DB7) : 0;

    mask = mask | v0 | v1 | v2 | v3 | v4 | v5 | v6 |v7;
    return mask;
}

void sleep_us_high_power(uint delay)
{
    uint64_t start = time_us_64();
    uint64_t expectedEnd = start + delay;
    uint64_t end = start;

    while (expectedEnd > end)
    {
        end = time_us_64();
    }
}

void wait_for_busy(uint readMask)
{
    uint eLow = make_command_mask(0, 1, false);
    uint eHigh = make_command_mask(0, 1, true);

    gpio_set_dir_out_masked(readMask);
    gpio_set_dir(PIN_DB7, false);

    gpio_put_masked(readMask, eLow);
    sleep_us_high_power(eCycleTime);

    bool busy = true;
    while (busy)
    {
        gpio_put_masked(readMask, eHigh);
        sleep_us_high_power(eCycleTime);
        
        gpio_put_masked(readMask, eLow);
        busy = gpio_get(PIN_DB7);
        sleep_us_high_power(eCycleTime);
    }
}

void put(uint writeMask, uint readMask, bool rs, bool rw, uint data, uint minDelay)
{
    uint eLow = make(rs, rw, false, data);
    uint eHigh = make(rs, rw, true, data);

    gpio_set_dir_out_masked(writeMask);

    gpio_put_masked(writeMask, eLow);
    sleep_us_high_power(eCycleTime);

    gpio_put_masked(writeMask, eHigh);
    sleep_us_high_power(eCycleTime);
    
    gpio_put_masked(writeMask, eLow);
    sleep_us_high_power(minDelay + eCycleTime);

    wait_for_busy(readMask);
}

void put_function(uint writeMask, uint readMask, bool eightBit, bool twoLines, bool largeFont)
{
    uint function = 0b00100000;
    uint f2 = eightBit ?  0b00010000 : 0;
    uint f1 = twoLines ?  0b00001000 : 0;
    uint f0 = largeFont ? 0b00000100 : 0;
    
    function = function | f2 | f1 | f0;
    put(writeMask, readMask, false, false, function, 39);
}

void put_display(uint writeMask, uint readMask, bool displayOn, bool cursorOn, bool cursoBlink)
{
    uint display =  0b00001000;  
    uint d2 = displayOn ?  0b00000100 : 0;
    uint d1 = cursorOn ?   0b00000010 : 0;
    uint d0 = cursoBlink ? 0b00000001 : 0;
    
    display = display | d2 | d1 | d0;
    put(writeMask, readMask, false, false, display, 39);
}

void put_shift(uint writeMask, uint readMask, bool increment, bool shiftDisplay)
{
    uint entry =  0b00000100;  //Display on; cursor on; blink off
    uint e1 = increment ?   0b00000010 : 0;
    uint e0 = shiftDisplay ? 0b00000001 : 0;
    
    entry = entry | e1 | e0;
    put(writeMask, readMask, false, false, entry, 39);
}

void put_clear(uint writeMask, uint readMask)
{
    put(writeMask, readMask, false, false, 0b00000001, 1530);
}

void put_string(uint writeMask, uint readMask, char* sz)
{
    char*   pc;

    pc = sz;
    while (*pc)
    {
        put(writeMask, readMask, true, false, *pc, 43);
        pc++;
    }
}

int main() 
{
    bi_decl(bi_program_description("This is a test binary."));

    stdio_init_all();

    sleep_us_high_power(1000);

    uint writeMask = make(true, true, true, 0xff);
    uint commandMask = make_command_mask(true, true, true);
    uint dataMask = make(false, false, false, 0xff);

    uint clear =    0b00000001;  //Clear display

    gpio_init_mask(writeMask);

    gpio_set_dir_out_masked(writeMask);

    gpio_init(PIN_LED);
    gpio_set_dir(PIN_LED, true);

    //Function must be 'put' 3 times to ensure that it is in the correct state.
    put_function(writeMask, commandMask, true, true, false);
    put_function(writeMask, commandMask, true, true, false);
    put_function(writeMask, commandMask, true, true, false);

    put_display(writeMask, commandMask, true, false, false);

    put_shift(writeMask, commandMask, true, false);

    bool led = true;
    while(true)
    {
        gpio_put(PIN_LED, led);
        
        put_clear(writeMask, commandMask);
        put_string(writeMask, commandMask, "Age of Chaos");
        sleep_us_high_power(1000000);

        put_clear(writeMask, commandMask);
        put_string(writeMask, commandMask, "Orcs and Humans");
        sleep_us_high_power(1000000);
        led = !led;
    }
}

