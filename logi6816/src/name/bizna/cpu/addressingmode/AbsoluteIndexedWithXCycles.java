package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;
import name.bizna.cpu.WidthFromRegister;

import static name.bizna.cpu.AddressingMode.AbsoluteIndexedWithX;

public class AbsoluteIndexedWithXCycles
    extends InstructionCycles
{
  //6a
  public AbsoluteIndexedWithXCycles(Executor<Cpu65816> operation, WidthFromRegister width)
  {
    super(AbsoluteIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(DBR(), AAH(), AAL_XL()), IO(), new NoteFourX(true)),
          new BusCycle(Address(DBR(), AA(), X()), Read_DataLow(), E8Bit(operation, width), DONE8Bit(width)),
          new BusCycle(Address(DBR(), AA(), X(), o(1)), Read_DataHigh(), E16Bit(operation, width), DONE16Bit(width)));
  }
}

