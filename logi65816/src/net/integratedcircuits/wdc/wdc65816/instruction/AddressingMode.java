package net.integratedcircuits.wdc.wdc65816.instruction;

public enum AddressingMode
{
  Absolute,
  AbsoluteLong,
  AbsoluteIndirect,
  AbsoluteIndirectLong,
  AbsoluteIndexedIndirectWithX,
  AbsoluteIndexedWithX,
  AbsoluteLongIndexedWithX,
  AbsoluteIndexedWithY,
  Accumulator,
  BlockMove,
  Direct,
  DirectIndexedWithX,
  DirectIndexedWithY,
  DirectIndirect,
  DirectIndirectLong,
  DirectIndexedIndirectWithX,
  DirectIndirectIndexedWithY,
  DirectIndirectLongIndexedWithY,
  OpCode,
  Implied,
  Immediate,
  StackInterruptSoftware,
  StackInterruptHardware,
  Stack,
  StackImmediate,
  StackDirectIndirect,
  StackRelative,
  StackRelativeIndirectIndexedWithY,
  StopTheClock,
  Relative,
  RelativeLong,
  WaitForInterrupt
}

