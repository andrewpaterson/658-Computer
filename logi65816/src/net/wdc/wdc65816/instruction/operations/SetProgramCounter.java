package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65C816;
import net.wdc.wdc65816.instruction.address.AddressOffset;

import java.util.Arrays;
import java.util.List;

public class SetProgramCounter
    extends Operation
{
  protected List<AddressOffset> addressOffsets;

  public SetProgramCounter(AddressOffset... addressOffsets)
  {
    this.addressOffsets = Arrays.asList(addressOffsets);
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
    cpu.setProgramCounter(AddressOffset.getAddress(cpu, addressOffsets));
  }

  @Override
  public String toString()
  {
    return "Set(PC)";
  }
}

