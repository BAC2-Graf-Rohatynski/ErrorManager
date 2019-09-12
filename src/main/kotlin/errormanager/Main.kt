package errormanager

import org.apache.log4j.BasicConfigurator

fun main(args: Array<String>) {
    BasicConfigurator.configure()
    ErrorManagerRunner.start()
}