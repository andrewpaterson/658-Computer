package name.bizna.emu65816;

public enum AddressingMode
{
  Interrupt,
  Accumulator,
  BlockMove,
  Implied,
  Immediate,
  Absolute,
  AbsoluteLong,
  AbsoluteIndirect,
  AbsoluteIndirectLong,
  AbsoluteIndexedIndirectWithX, // index with X
  AbsoluteIndexedWithX,
  AbsoluteLongIndexedWithX,
  AbsoluteIndexedWithY,
  Direct,
  DirectIndexedWithX,
  DirectIndexedWithY,
  DirectIndirect,
  DirectIndirectLong,
  DirectIndexedIndirectWithX,
  DirectIndirectIndexedWithY,
  DirectIndirectLongIndexedWithY,
  StackImplied,
  StackRelative,
  StackAbsolute,
  StackDirectPageIndirect,
  StackProgramCounterRelativeLong,
  StackRelativeIndirectIndexedWithY,
  ProgramCounterRelative,
  ProgramCounterRelativeLong
}

