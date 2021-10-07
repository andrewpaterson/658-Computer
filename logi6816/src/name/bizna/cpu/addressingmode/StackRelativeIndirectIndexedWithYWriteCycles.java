package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Width;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.StackRelativeIndirectIndexedWithY;

public class StackRelativeIndirectIndexedWithYWriteCycles
    extends InstructionCycles
{
  public StackRelativeIndirectIndexedWithYWriteCycles(Consumer<Cpu65816> operation)
  {
    super(StackRelativeIndirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), SP_inc()),
          new BusCycle(Address(S(), D0()), Read_AAL(), SP_inc()),
          new BusCycle(Address(S(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(S(), D0(), o(1)), IO()),
          new BusCycle(Address(DBR(), AA(), Y()), E(operation), Write_DataLow(), DONE8Bit(Width.A)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), Write_DataHigh(), DONE16Bit(Width.A)));
  }
}

