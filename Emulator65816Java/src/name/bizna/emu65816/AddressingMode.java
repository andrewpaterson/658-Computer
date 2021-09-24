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
  DirectPage,
  DirectPageIndexedWithX,
  DirectPageIndexedWithY,
  DirectPageIndirect,
  DirectPageIndirectLong,
  DirectPageIndexedIndirectWithX,
  DirectPageIndirectIndexedWithY,
  DirectPageIndirectLongIndexedWithY,
  StackImplied,
  StackRelative,
  StackAbsolute,
  StackDirectPageIndirect,
  StackProgramCounterRelativeLong,
  StackRelativeIndirectIndexedWithY,
  ProgramCounterRelative,
  ProgramCounterRelativeLong
}

