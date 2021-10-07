package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Width;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.Absolute;

public class AbsoluteWriteCycles
    extends InstructionCycles
{
  //1a
  public AbsoluteWriteCycles(Consumer<Cpu65816> operation, Width width)
  {
    super(Absolute,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(DBR(), AA()), E(operation), Write_DataLow(), DONE8Bit(width)),
          new BusCycle(Address(DBR(), AA(), o(1)), Write_DataHigh(), DONE16Bit(width)));
  }
}

