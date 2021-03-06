package br.ucsal.banco.database

import androidx.room.*
import java.util.*

/**
 * https://developer.android.com/training/data-storage/room/relationships
 */


/**
 * Endereco
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EmpresaCliente::class,
            parentColumns = ["id"],
            childColumns = ["empresa_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class Endereco(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val rua: String,
    val estado: String,
    val cidade: String,
    val cep: String,
    val numero: String,
    val bairro: String,
    @ColumnInfo(name = "empresa_id") val empresaId: Long
)


/**
 * Empresa
 */
@Entity(tableName = "empresa_cliente", indices = [Index(value = ["cnpj", "nome"], unique = true)])
data class EmpresaCliente(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val nome: String,
    val cnpj: String,
)


/**
 * Funcionario
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = EmpresaCliente::class,
            parentColumns = ["id"],
            childColumns = ["empresa_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class Funcionario(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val nome: String,
    val cpf: String,
    @ColumnInfo(name = "empresa_id") val empresaId: Long
)


/**
 * Exame
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Funcionario::class,
            parentColumns = ["id"],
            childColumns = ["funcionario_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Atestado::class,
            parentColumns = ["id"],
            childColumns = ["atestado_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Medico::class,
            parentColumns = ["id"],
            childColumns = ["medico_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Exame(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val data: Date,
    val relatorio: String,
    @ColumnInfo(name = "funcionario_id") val funcionarioId: Long,
    @ColumnInfo(name = "atestado_id") val atestadoId: Long,
    @ColumnInfo(name = "medico_id") val medicoId: Long
)

@Entity(
    tableName = "tipo_exame", foreignKeys = [
        ForeignKey(
            entity = Exame::class,
            parentColumns = ["id"],
            childColumns = ["exame_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TipoExame(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val nome: String,
    @ColumnInfo(name = "exame_id") val exameId: Long
)


/**
 * Medico
 */
@Entity
data class Medico(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val nome: String,
    val crm: String
)

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TipoAtestado::class,
            parentColumns = ["id"],
            childColumns = ["empresa_id"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)


/**
 * Atestado
 */
data class Atestado(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val descricao: String,
    @ColumnInfo(name = "tipo_atestado_id") val tipoAtestadoId: Long
)

@Entity(tableName = "tipo_atestado")
data class TipoAtestado(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val nome: String,
)

/**
 * https://developer.android.com/training/data-storage/room/referencing-data
 */
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}