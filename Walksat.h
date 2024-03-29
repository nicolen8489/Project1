/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class Walksat */

#ifndef _Included_Walksat
#define _Included_Walksat
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     Walksat
 * Method:    runWalkSat
 * Signature: ([C[IILDataInfo;)Z
 */
JNIEXPORT jboolean JNICALL Java_Walksat_runWalkSat
  (JNIEnv *, jclass, jcharArray, jintArray, jint, jobject);

/*
 * Class:     Walksat
 * Method:    setNumberOfSolutions
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_Walksat_setNumberOfSolutions
  (JNIEnv *, jclass, jint);

/*
 * Class:     Walksat
 * Method:    setNumberOfTries
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_Walksat_setNumberOfTries
  (JNIEnv *, jclass, jint);

#ifdef __cplusplus
}
#endif
#endif
