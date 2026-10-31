# HCP65 Computer

The HCP65 computer, built around the W65C816.

The processor board holds the W65C816, 512KB of RAM, 512KB of flash ROM and a set of on-board
registers.  Everything else (memory boards, video, sound, storage, input) plugs into device sockets.

- [Memory Map](memory-map.md) - kernel and user mode, what is where, and how user processes get
  their own banks.
- [On-board Registers](registers.md) - the 16 registers on the processor board: interrupts, the
  NMI timer, device resets, the process ID and the instruction counter.
- [Devices and Main Memory](devices.md) - the device sockets, the layout inside each device's
  space, and how main memory is assigned to the sockets.

These pages were worked out from the KiCad schematics in `658-Computer/Kicad/Computer`.  The
schematic sheet each part comes from is named in its section.
