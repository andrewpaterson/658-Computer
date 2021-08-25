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

uint make(bool rs, bool rw, bool e, uint val)
{
    uint rs_s = rs ? (1 << PIN_REGISTER_SELECT) : 0;
    uint rw_s = rw ? (1 << PIN_READ_WRITEB) : 0;
    uint e_s = e ? (1 << PIN_ENABLE) : 0;
    uint v0 = val & 0x01 ? (1 << PIN_DB0) : 0;
    uint v1 = val & 0x02 ? (1 << PIN_DB1) : 0;
    uint v2 = val & 0x04 ? (1 << PIN_DB2) : 0;
    uint v3 = val & 0x08 ? (1 << PIN_DB3) : 0;
    uint v4 = val & 0x10 ? (1 << PIN_DB4) : 0;
    uint v5 = val & 0x20 ? (1 << PIN_DB5) : 0;
    uint v6 = val & 0x40 ? (1 << PIN_DB6) : 0;
    uint v7 = val & 0x80 ? (1 << PIN_DB7) : 0;

    uint mask =  rs_s | rw_s | e_s | v0 | v1 | v2 | v3 | v4 | v5 | v6 |v7;
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

void put(uint mask, bool rs, bool rw, uint data)
{
    uint eLow = make(rs, rw, false, data);
    uint eHigh = make(rs, rw, true, data);

    gpio_put_masked(mask, eLow);
    sleep_us_high_power(5);

    gpio_put_masked(mask, eHigh);
    sleep_us_high_power(5000);
    
    gpio_put_masked(mask, eLow);
    sleep_us_high_power(5000);
}


int main() 
{
    bi_decl(bi_program_description("This is a test binary."));

    stdio_init_all();

    sleep_us_high_power(1000);

    uint mask = make(true, true, true, 0xff);

    uint function = 0b00111000;  //8-bit mode; 2-lines; 5x8 font
    uint clear =    0b00000001;  //Clear display
    uint display =  0b00001100;  //Display on; cursor on; blink off
    uint cursor =   0b00000110;  //Increment and shift cursor; don't shift display

    gpio_init_mask(mask);

    gpio_set_dir_out_masked(mask);

    gpio_init(PIN_LED);
    gpio_set_dir(PIN_LED, true);

    put(mask, false, false, function);
    put(mask, false, false, function);
    put(mask, false, false, function);
    put(mask, false, false, display);
    put(mask, false, false, cursor);

    bool led = true;
    while(true)
    {
        gpio_put(PIN_LED, led);
        
        put(mask, false, false, clear);
        put(mask, true, false, 'H');
        put(mask, true, false, 'e');
        put(mask, true, false, 'l');
        put(mask, true, false, 'l');
        put(mask, true, false, 'o');
        sleep_us_high_power(1000000);

        put(mask, false, false, clear);
        put(mask, true, false, 'W');
        put(mask, true, false, 'o');
        put(mask, true, false, 'r');
        put(mask, true, false, 'l');
        put(mask, true, false, 'd');
        sleep_us_high_power(1000000);
        led = !led;
    }
}

