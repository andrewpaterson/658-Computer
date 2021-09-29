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
  Implied,
  Immediate,
  StackInterruptSoftware,
  StackInterruptHardware,
  StackImplied,
  StackRelative,
  StackAbsolute,
  StackDirectIndirect,
  StackRelativeIndirectIndexedWithY,
  Relative,
  RelativeLong
}

