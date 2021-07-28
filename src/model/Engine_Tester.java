package model;

import java.util.List;

public class Engine_Tester
{

	public static void main(String[] args) throws Exception
	{
		Engine engine = Engine.getInstance();
		List<StudentBean> list = engine.doSis("A", "3", "COURSES");
		for(StudentBean e: list)
//		{
//			System.out.println(e);
//		}
		System.out.println(engine.doDrone("4700 Keele Street", "5000 Yonge"));

	}

}
