package com.fastcampus.commerce.aws

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

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

class S3ImageUploadTest :
    BehaviorSpec({
        given("S3 클라이언트와 테스트 이미지가 주어졌을 때") {
            val s3Client = S3Client.builder().build()
            val buckets = s3Client.listBuckets().buckets()
            val bucketName = buckets.firstOrNull()?.name()
                ?: throw IllegalStateException("S3 버킷을 찾을 수 없습니다.")
            val keyName = UUID.randomUUID().toString()

            // 테스트용 이미지 파일 생성
            val testImage = File.createTempFile("/thumbnail/$keyName", ".png")
            S3TestUtils.createTestImage(testImage)

            afterTest {
                s3Client.deleteObject(
                    DeleteObjectRequest
                        .builder()
                        .bucket(bucketName)
                        .key(keyName)
                        .build(),
                )
                testImage.delete()
            }

            `when`("이미지를 S3에 업로드하면") {
                val putObjectRequest = PutObjectRequest
                    .builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType("image/png")
                    .build()

                val response = s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromFile(testImage),
                )

                then("업로드가 성공해야 한다") {
                    response.sdkHttpResponse().isSuccessful shouldBe true
                }
            }
        }
    })

class S3ObjectTest :
    BehaviorSpec({
        given("S3 클라이언트와 업로드된 이미지가 주어졌을 때") {
            val s3Client = S3Client.builder().build()
            val buckets = s3Client.listBuckets().buckets()
            val bucketName = buckets.firstOrNull()?.name()
                ?: throw IllegalStateException("S3 버킷을 찾을 수 없습니다.")
            val keyName = "test-image-${UUID.randomUUID()}.png"

            // 테스트용 이미지 파일 생성 및 업로드
            val testImage = File.createTempFile("test-image", ".png")
            S3TestUtils.createTestImage(testImage)

            val putObjectRequest = PutObjectRequest
                .builder()
                .bucket(bucketName)
                .key(keyName)
                .contentType("image/png")
                .build()

            s3Client.putObject(putObjectRequest, RequestBody.fromFile(testImage))

            afterTest {
                s3Client.deleteObject(
                    DeleteObjectRequest
                        .builder()
                        .bucket(bucketName)
                        .key(keyName)
                        .build(),
                )
                testImage.delete()
            }

            `when`("객체를 조회하면") {
                val getObjectRequest = GetObjectRequest
                    .builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .build()

                val response = s3Client.getObject(getObjectRequest)

                then("객체가 성공적으로 조회되어야 한다") {
                    response.response().contentType() shouldBe "image/png"
                    response.response().contentLength().toLong() shouldBeGreaterThan 0L
                }
            }
        }
    })

object S3TestUtils {
    fun createTestImage(file: File) {
        val width = 100
        val height = 100
        val image = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val graphics = image.graphics

        graphics.color = Color.WHITE
        graphics.fillRect(0, 0, width, height)
        graphics.color = Color.BLACK
        graphics.drawRect(10, 10, 80, 80)

        ImageIO.write(image, "png", file)
    }
}
