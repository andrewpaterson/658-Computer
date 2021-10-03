package name.bizna.emu65816;

public enum AddressingMode
{
  Absolute,
  AbsoluteLong,
  AbsoluteIndirect,
  AbsoluteIndirectLong,
  AbsoluteIndexedIndirectWithX, // index with X
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

