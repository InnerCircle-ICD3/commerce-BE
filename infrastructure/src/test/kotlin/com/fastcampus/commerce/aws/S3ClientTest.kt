package com.fastcampus.commerce.aws

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import software.amazon.awssdk.services.s3.S3Client

class S3ClientTest :
    BehaviorSpec({
        given("S3 클라이언트가 주어졌을 때") {
            val s3Client = S3Client
                .builder()
                .build()

            `when`("모든 버킷을 조회하면") {
                val buckets = s3Client.listBuckets().buckets()

                then("버킷 목록이 반환되어야 한다") {
                    buckets.shouldNotBeEmpty()

                    // 디버깅용 출력
                    println("AWS S3 버킷 목록:")
                    buckets.forEach { bucket ->
                        println("\t${bucket.name()}")
                    }
                }
            }
        }
    })
