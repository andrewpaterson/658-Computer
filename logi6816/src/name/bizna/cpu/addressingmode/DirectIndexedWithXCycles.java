package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;
import name.bizna.cpu.WidthFromRegister;

import static name.bizna.cpu.AddressingMode.DirectIndexedWithX;

public class DirectIndexedWithXCycles
    extends InstructionCycles
{
  //16a
  public DirectIndexedWithXCycles(Executor<Cpu65816> operation, WidthFromRegister width)
  {
    super(DirectIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(DP(), D0(), X()), Read_DataLow(), E8Bit(operation, width), DONE8Bit(width)),
          new BusCycle(Address(DP(), D0(), X(), o(1)), Read_DataHigh(), E16Bit(operation, width), DONE16Bit(width)));
  }
}

