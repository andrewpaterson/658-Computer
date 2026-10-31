# Memory Map

The W65C816 has a 24 bit address bus: 256 banks of 64KB, 16MB in all.  What those addresses reach
depends on whether the processor is in kernel mode or user mode.

## Kernel mode and user mode

The processor board counts how deeply it is nested in interrupts (*HCP65 Kernal Mode* sheet).

- The count goes up by one each time the processor fetches an interrupt vector: IRQ, NMI, BRK, COP,
  ABORT or RESET.
- It goes down by one each time an `RTI` (opcode `$40`) is executed.
- A count of **zero is user mode**.  Any count **above zero is kernel mode**.

Reset clears the count, and fetching the reset vector counts it up to one, so the processor starts
in kernel mode.  The kernel starts a user process by returning to it with `RTI`.

The count can be read and written through the [Interrupt Depth](registers.md#9-interrupt-depth)
register.

## Kernel mode

In kernel mode the bottom 2MB holds the processor board's own memory and the devices, and main
memory starts at 2MB (*HCP65 MPU Address Decode* sheet).

| Address | Size | What |
|---|---|---|
| `$000000`–`$07FFFF` | 512KB | MPU RAM |
| `$080000`–`$0FFFFF` | 512KB | MPU ROM |
| `$100000`–`$107FFF` | 32KB | [On-board registers](registers.md) (device slot 0) |
| `$108000`–`$13FFFF` | 7 × 32KB | [Devices 1–7](devices.md#device-sockets) |
| `$140000`–`$17FFFF` | 256KB | Nothing |
| `$180000`–`$1FFFFF` | 4 × 128KB | [Devices 16–19](devices.md#device-sockets) |
| `$200000`–`$FFFFFF` | 14MB | [Main memory](devices.md#main-memory) |

The kernel cannot reach the first 2MB of main memory; that part is only reachable from user mode.

The MPU RAM is an AS7C34096 static RAM.  The MPU ROM is an SST39LF040 flash chip whose write line is
connected, so it can be reprogrammed in place using the flash chip's command sequences.

### RAM and ROM swap at boot

The processor starts in emulation mode, and its reset vector is read from the top of bank 0.  For
that to come from ROM, the RAM and ROM chip selects start out swapped:

| | Before the first switch to native mode | After it |
|---|---|---|
| `$000000`–`$07FFFF` | MPU ROM | MPU RAM |
| `$080000`–`$0FFFFF` | MPU RAM | MPU ROM |

A latch on the *W65C816 Breakout* sheet is set when the processor's E output first goes low (the
first `XCE` into native mode) and is only cleared by reset.  So the boot code copies the 512KB ROM
into the 512KB RAM while still in emulation mode, then switches to native mode, and carries on
running from the same addresses, now in RAM.

The [NMI timer](registers.md#6-timer-speed-select) is also held stopped until this latch is set.

## User mode

In user mode there are no devices, no MPU RAM and no MPU ROM: every address goes to
[main memory](devices.md#main-memory), all 16MB of it.

### Bank remapping

Each user process sees its own banks.  On every access the processor board replaces the bank
byte (address bits A16–A23) with a byte looked up in a table in MPU RAM:

```
physical bank = MPU RAM [ $070000 + ProcessID × $100 + bank ]
```

The table is the top 64KB of MPU RAM: one 256 byte page per process, one byte per bank.  The
[Process ID](registers.md#8-process-id) register picks the page.  For example, if process 5's
page holds `$42` at offset 0, then process 5's bank 0 is physical bank `$42` of main memory.

The lookup happens in the first half of each bus cycle, while PHI2 is low.  No remapping is done in
kernel mode; the kernel sees physical addresses.
