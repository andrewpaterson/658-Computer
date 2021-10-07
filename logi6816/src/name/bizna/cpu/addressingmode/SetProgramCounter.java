package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;

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
  public void execute(Cpu65816 cpu)
  {
    cpu.setProgramAddress(AddressOffset.getAddress(cpu, addressOffsets));
  }

  @Override
  public String toString()
  {
    return "Set(PC)";
  }
}

