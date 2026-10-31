# Devices and Main Memory

## Device sockets

Devices are boards plugged into the mainboard's device sockets.  In
[kernel mode](memory-map.md#kernel-mode) each socket has its own slot of the address space
(*HCP65 MPU Address Decode* sheet).  User mode cannot reach the devices at all.

| Device | Address | Size |
|---|---|---|
| 0 | `$100000`–`$107FFF` | 32KB: the processor board's [on-board registers](registers.md) |
| 1 | `$108000`–`$10FFFF` | 32KB |
| 2 | `$110000`–`$117FFF` | 32KB |
| 3 | `$118000`–`$11FFFF` | 32KB |
| 4 | `$120000`–`$127FFF` | 32KB |
| 5 | `$128000`–`$12FFFF` | 32KB |
| 6 | `$130000`–`$137FFF` | 32KB |
| 7 | `$138000`–`$13FFFF` | 32KB |
| 16 | `$180000`–`$19FFFF` | 128KB |
| 17 | `$1A0000`–`$1BFFFF` | 128KB |
| 18 | `$1C0000`–`$1DFFFF` | 128KB |
| 19 | `$1E0000`–`$1FFFFF` | 128KB |

Devices 8–15 do not exist.

Each device also has a reset line and an interrupt line, controlled and read through the on-board
[Device Reset/Enable](registers.md#3-and-5-device-resetenable) and
[Device Interrupt](registers.md#0-and-2-device-interrupt) registers.  Every device is held in reset
until the kernel enables it.

On the small mainboard, device 1 is the [main memory channel](#main-memory) and device 7 is the
processor reset device.  The other device boards (VIA, UART, PS/2, SD card, SNES controller, LCD,
sound, video, graphics, blitter, coprocessor) can go in any socket.

## Inside a device's slot

Most device boards use the standard two stage device decoder (*Device Decode stage A* and
*Device Decode stage B* sheets).  It divides the device's slot like this:

| Offset | Size | What |
|---|---|---|
| `$00000`–`$003FF` | 1KB | Device ROM: the device's identifier string |
| `$00400`–`$007FF` | 1KB | Device registers |
| `$00800`–end of slot | Up to ~126KB | Device RAM, if the device has any |

A 32KB slot ends at `$07FFF` and a 128KB slot at `$1FFFF`.

The device registers are decoded from A0–A4: A0–A2 pick one of 8 registers, and A3–A4 pick one of
up to four groups of 8, so a device can have up to 32 registers.  They repeat through the 1KB.  Each
register can have separate read and write meanings.

## Main memory

Main memory is a 16MB space.  It is made of 2MB memory chips on memory boards, which plug into device
sockets: two chips per socket, so 4MB per socket.

- In [user mode](memory-map.md#user-mode) every address is main memory, after
  [bank remapping](memory-map.md#bank-remapping).
- In kernel mode main memory is `$200000`–`$FFFFFF`; the first 2MB of main memory is only reachable
  from user mode.

### Main memory channel

Which 4MB of main memory a socket's two chips answer is set by the main memory channel, which is
device 1 (*Main Memory Channel* and *Main Memory Select* sheets).  Each socket gets a 2 bit window
number:

| Window | Main memory |
|---|---|
| 0 | `$000000`–`$3FFFFF` |
| 1 | `$400000`–`$7FFFFF` |
| 2 | `$800000`–`$BFFFFF` |
| 3 | `$C00000`–`$FFFFFF` |

Within the window, the socket's first chip is the lower 2MB and its second chip the upper 2MB.

The window numbers are set by writing two registers at the start of device 1's slot.  These are
written directly, not through the standard device decoder, and they repeat every 8 bytes through
the slot.

| Register | Address | D1–D0 | D3–D2 | D5–D4 | D7–D6 |
|---|---|---|---|---|---|
| 0 | `$108000` | Socket S | Socket T | Socket U (device 19) | Socket V (device 18) |
| 1 | `$108001` | Socket W (device 4) | Socket X (device 3) | Socket Y (device 17) | Socket Z (device 16) |

Sockets S and T are not wired on the small mainboard.  In each pair the lower data bit is the low
bit of the window number, so writing `%01` to a pair selects window 1.

