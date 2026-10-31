# On-board Registers

The processor board's registers are device slot 0, at `$100000` in
[kernel mode](memory-map.md#kernel-mode).  Only address bits A0–A3 are decoded, so the 16 registers
repeat every 16 bytes through to `$107FFF`.

| Register | Address | Name | Access |
|---|---|---|---|
| 0 | `$100000` | [Device Interrupt 1–7](#0-and-2-device-interrupt) | Read |
| 1 | `$100001` | Not connected | |
| 2 | `$100002` | [Device Interrupt 16–19](#0-and-2-device-interrupt) | Read |
| 3 | `$100003` | [Device Reset/Enable 1–7](#3-and-5-device-resetenable) | Read and write |
| 4 | `$100004` | Not connected | |
| 5 | `$100005` | [Device Reset/Enable 16–19](#3-and-5-device-resetenable) | Read and write |
| 6 | `$100006` | [Timer Speed Select](#6-timer-speed-select) | Read and write |
| 7 | `$100007` | [NMI State](#7-nmi-state) | Read and write |
| 8 | `$100008` | [Process ID](#8-process-id) | Read and write |
| 9 | `$100009` | [Interrupt Depth](#9-interrupt-depth) | Read and write |
| 10 | `$10000A` | [Board State](#10-board-state) | Read |
| 11 | `$10000B` | [Clear Counter](#11-to-15-instruction-counter) | Write |
| 12–15 | `$10000C`–`$10000F` | [Instruction Count](#11-to-15-instruction-counter) | Read; writing 12 clears |

## 0 and 2: Device Interrupt

*HCP65 Device Interrupts* sheet.

Each device has an active-low interrupt line.  These registers read them, so a **0 bit means that
device is interrupting**.

| Register | D7 | D6 | D5 | D4 | D3 | D2 | D1 | D0 |
|---|---|---|---|---|---|---|---|---|
| 0 | Device 7 | Device 6 | Device 5 | Device 4 | Device 3 | Device 2 | Device 1 | |
| 2 | | | | | Device 19 | Device 18 | Device 17 | Device 16 |

The processor's IRQ input is low while any device's line is low.  The device clears its own
interrupt; there is nothing to acknowledge here.

## 3 and 5: Device Reset/Enable

*HCP65 Device Reset* sheet.

Writing sets each device's reset line; reading returns the current setting.  A **0 holds the device
in reset** and a **1 lets it run**.  Reset clears both registers, so every device stays in reset
until the kernel enables it.

| Register | D7 | D6 | D5 | D4 | D3 | D2 | D1 | D0 |
|---|---|---|---|---|---|---|---|---|
| 3 | Device 7 | Device 6 | Device 5 | Device 4 | Device 3 | Device 2 | Device 1 | |
| 5 | | | | | Device 19 | Device 18 | Device 17 | Device 16 |

## 6: Timer Speed Select

*HCP65 MPU Timer* sheet.

The timer raises an NMI at a regular rate.  D0–D2 select the rate; reading returns them.  Reset
sets 0.

| D2–D0 | NMI every | At 32.768kHz |
|---|---|---|
| 0 | 2<sup>21</sup> clocks | 64 s |
| 1 | 2<sup>19</sup> clocks | 16 s |
| 2 | 2<sup>17</sup> clocks | 4 s |
| 3 | 2<sup>15</sup> clocks | 1 s |
| 4 | 2<sup>13</sup> clocks | 250 ms |
| 5 | 2<sup>11</sup> clocks | 62.5 ms |
| 6 | 2<sup>9</sup> clocks | 15.625 ms |
| 7 | 2<sup>7</sup> clocks | 3.90625 ms |

The timer's clock is a 32.768kHz oscillator, so the period is 2<sup>21 − 2 × speed</sup> / 32768 seconds: 64 s at speed 0, a quarter of that for each step up.  The timer does not run until
the processor has first switched to native mode (see
[RAM and ROM swap at boot](memory-map.md#ram-and-rom-swap-at-boot)).

## 7: NMI State

*HCP65 MPU NMI* sheet.

The NMI has four sources: the timer and three break inputs.  Each source is latched when it fires,
and the processor's NMI line is low while any source is latched and NMIs are enabled.

Reading:

| Bit | Meaning |
|---|---|
| D0 | Timer has fired |
| D1 | Break 1 has fired |
| D2 | Break 2 has fired |
| D3 | Break 3 has fired |
| D4 | NMIs are enabled |
| D6 | MPU0 |
| D7 | MPU1 |

D6 and D7 identify which processor this is when more than one is fitted.

Writing (a 0 bit does nothing):

| Bit | Writing a 1 |
|---|---|
| D0 | Clears the timer source |
| D1 | Clears break 1 |
| D2 | Clears break 2 |
| D3 | Clears break 3 |
| D4 | Enables NMIs |
| D5 | Disables NMIs |

A source is cleared even if its input is still active.  NMIs are enabled after reset.

## 8: Process ID

*HCP65 Kernal Mode* sheet.

The current user process, 0–255.  It selects the process's page of the
[bank remapping table](memory-map.md#bank-remapping).

## 9: Interrupt Depth

*HCP65 Kernal Mode* sheet.

The 7 bit [kernel mode count](memory-map.md#kernel-mode-and-user-mode), on D0–D6.  It counts up on
each interrupt vector fetch and down on each `RTI`; zero is user mode.  Writing loads it, which lets
the kernel switch processes or unwind nested interrupts.

## 10: Board State

*HCP65 Board Presence* sheet.

Says which optional sub-boards are fitted to the processor board.  The register has two bytes and
each access moves to the other one: the first read returns presence bits S0–S5 on D2–D7, the next
S6–S11 on D2–D7.  S0 is the timer and S4 is the kernel mode board; the other bits are not yet
documented.

## 11 to 15: Instruction Counter

*HCP65 Instruction Code* sheet.

A 32 bit count of the instructions executed (opcode fetches).

- Reading register 12 takes a snapshot of the whole count and returns its low byte.
- Registers 13, 14 and 15 then return bytes 1, 2 and 3 of that snapshot.
- Writing register 11 (Clear Counter) or register 12 clears the count.  Reset clears it too.
